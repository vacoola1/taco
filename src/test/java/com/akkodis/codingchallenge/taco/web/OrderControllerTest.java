package com.akkodis.codingchallenge.taco.web;

import com.akkodis.codingchallenge.taco.service.OrderService;
import com.akkodis.codingchallenge.taco.service.exceptions.NotEnoughAmountException;
import com.akkodis.codingchallenge.taco.web.api.OrderController;
import com.akkodis.codingchallenge.taco.web.dto.request.OrderItemRequestDto;
import com.akkodis.codingchallenge.taco.web.dto.request.OrderRequestDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import(GlobalExceptionHandler.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService mockOrderService;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(mockOrderService);
    }

    @Test
    @DisplayName("Order tacos successfully and return order ID")
    void testOrderTacos() throws Exception {

        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .orderItem(
                        OrderItemRequestDto.builder()
                                .tacoId(1L)
                                .toppingId(2L)
                                .toppingId(3L)
                                .count(2)
                                .build()
                )
                .orderItem(
                        OrderItemRequestDto.builder()
                                .tacoId(2L)
                                .toppingId(4L)
                                .toppingId(5L)
                                .count(3)
                                .build())
                .build();

        when(mockOrderService.orderTacos(orderRequestDto)).thenReturn(12345L);

        String requestBody = """
                {
                    "orderItems": [
                        {
                            "tacoId": 1,
                            "toppingIds": [2, 3],
                            "count": 2
                        },
                        {
                            "tacoId": 2,
                            "toppingIds": [4, 5],
                            "count": 3
                        }
                    ]
                }
                """;

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("12345"));

        verify(mockOrderService).orderTacos(orderRequestDto);
    }

    @Test
    @DisplayName("Handle EntityNotFoundException and return 404 Not Found status")
    void testEntityNotFoundException() throws Exception {
        OrderRequestDto orderRequestDto = createSampleOrderRequest();

        when(mockOrderService.orderTacos(orderRequestDto)).thenThrow(new EntityNotFoundException("Order not found"));

        String requestBody = createSampleRequestBody();

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                //.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found"));

        verify(mockOrderService).orderTacos(orderRequestDto);
    }

    @Test
    @DisplayName("Handle NotEnoughAmountException and return 406 Not Acceptable status")
    void testNotEnoughAmountException() throws Exception {
        OrderRequestDto orderRequestDto = createSampleOrderRequest();

        when(mockOrderService.orderTacos(orderRequestDto)).thenThrow(new NotEnoughAmountException("Not enough funds"));

        String requestBody = createSampleRequestBody();

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                //.andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(content().string("Not enough funds"));

        verify(mockOrderService).orderTacos(orderRequestDto);
    }

    @Test
    @DisplayName("Handle generic RuntimeException and return 500 Internal Server Error status")
    void testGenericRuntimeException() throws Exception {
        OrderRequestDto orderRequestDto = createSampleOrderRequest();

        when(mockOrderService.orderTacos(orderRequestDto)).thenThrow(new RuntimeException());

        String requestBody = createSampleRequestBody();

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                //.andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(INTERNAL_SERVER_ERROR.getReasonPhrase()));

        verify(mockOrderService).orderTacos(orderRequestDto);
    }

    private OrderRequestDto createSampleOrderRequest() {
        return OrderRequestDto.builder()
                .orderItem(OrderItemRequestDto.builder()
                        .tacoId(1L)
                        .toppingIds(Arrays.asList(2L, 3L))
                        .count(2)
                        .build()
                ).build();
    }

    private String createSampleRequestBody() {
        return """
                {
                    "orderItems": [
                        {
                            "tacoId": 1,
                            "toppingIds": [2, 3],
                            "count": 2
                        }
                    ]
                }
                """;
    }

}
