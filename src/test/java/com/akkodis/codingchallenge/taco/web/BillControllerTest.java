package com.akkodis.codingchallenge.taco.web;

import com.akkodis.codingchallenge.taco.model.Bill;
import com.akkodis.codingchallenge.taco.service.BillService;
import com.akkodis.codingchallenge.taco.web.api.BillController;
import com.akkodis.codingchallenge.taco.web.dto.response.BillDto;
import com.akkodis.codingchallenge.taco.web.dto.assembler.BillDtoAssembler;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BillController.class)
@Import(GlobalExceptionHandler.class)
class BillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillService mockBillService;

    @MockBean
    private BillDtoAssembler mockBillDtoAssembler;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(mockBillService, mockBillDtoAssembler);
    }

    @Test
    @DisplayName("Calculate and retrieve bill details for a given order ID")
    void testCalculateBill() throws Exception {
        Long orderId = 12345L;
        Bill bill = new Bill();
        bill.setOrderId(orderId);
        bill.setTotalWithoutTax(100.0);
        bill.setStateTax(10.0);
        bill.setFederalTax(5.0);
        bill.setTotalWithTax(115.0);

        BillDto billDto = BillDto.builder()
                .orderId(orderId)
                .totalWithoutTax(100.0)
                .stateTax(10.0)
                .federalTax(5.0)
                .totalWithTax(115.0)
                .build();

        when(mockBillService.calculateBill(orderId)).thenReturn(bill);
        when(mockBillDtoAssembler.toModel(bill)).thenReturn(billDto);

        String expectedResponse = """
            {
                "orderId": 12345,
                "totalWithoutTax": 100.0,
                "stateTax": 10.0,
                "federalTax": 5.0,
                "totalWithTax": 115.0
            }
            """;

        mockMvc.perform(post("/api/v1/bills/" + orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(mockBillService).calculateBill(orderId);
        verify(mockBillDtoAssembler).toModel(bill);
    }

    @Test
    @DisplayName("Handle EntityNotFoundException and return 404 Not Found status")
    void testEntityNotFoundException() throws Exception {
        Long orderId = 12345L;

        when(mockBillService.calculateBill(orderId))
                .thenThrow(new EntityNotFoundException("Order not found by orderId=%s".formatted(orderId)));

        mockMvc.perform(post("/api/v1/bills/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found by orderId=%s".formatted(orderId)));

        verify(mockBillService).calculateBill(orderId);
    }

}
