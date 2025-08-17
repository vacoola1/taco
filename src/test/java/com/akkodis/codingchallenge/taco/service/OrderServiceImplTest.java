package com.akkodis.codingchallenge.taco.service;

import com.akkodis.codingchallenge.taco.dao.OrderRepository;
import com.akkodis.codingchallenge.taco.model.Order;
import com.akkodis.codingchallenge.taco.model.OrderItem;
import com.akkodis.codingchallenge.taco.model.Taco;
import com.akkodis.codingchallenge.taco.model.Topping;
import com.akkodis.codingchallenge.taco.service.exceptions.NotEnoughAmountException;
import com.akkodis.codingchallenge.taco.web.dto.request.OrderItemRequestDto;
import com.akkodis.codingchallenge.taco.web.dto.request.OrderRequestDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository mockOrderRepository;
    @Mock
    private TacoService mockTacoService;
    @Mock
    private ToppingService mockToppingService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(mockOrderRepository, mockTacoService, mockToppingService);
    }

    @Test
    @DisplayName("Should place an order and reduce taco availability for multiple items")
    void orderTacos() {
        // Given
        OrderItemRequestDto orderItemRequest1 = OrderItemRequestDto.builder()
                .tacoId(1L)
                .toppingIds(List.of(1L, 2L))
                .count(2)
                .build();

        OrderItemRequestDto orderItemRequest2 = OrderItemRequestDto.builder()
                .tacoId(2L)
                .toppingIds(List.of(1L))
                .count(3)
                .build();

        OrderRequestDto request = OrderRequestDto.builder()
                .orderItems(List.of(orderItemRequest1, orderItemRequest2))
                .build();

        Taco taco1 = new Taco(1L, "Taco 1", 10.0, 10);
        Taco taco2 = new Taco(2L, "Taco 2", 12.0, 15);
        Topping topping1 = new Topping(1L, "Topping 1", 1.0);
        Topping topping2 = new Topping(2L, "Topping 2", 2.0);

        OrderItem expectedOrderItem1 = new OrderItem();
        expectedOrderItem1.setTaco(taco1);
        expectedOrderItem1.setToppings(List.of(topping1, topping2));
        expectedOrderItem1.setQuantity(2);

        OrderItem expectedOrderItem2 = new OrderItem();
        expectedOrderItem2.setTaco(taco2);
        expectedOrderItem2.setToppings(List.of(topping1));
        expectedOrderItem2.setQuantity(3);

        Order expectedOrder = new Order();
        expectedOrder.setOrderItems(List.of(expectedOrderItem1, expectedOrderItem2));

        Order savedOrder = new Order();
        savedOrder.setId(1L);

        Taco savedTaco1 = new Taco(1L, "Taco 1", 10.0, 8);
        Taco savedTaco2 = new Taco(2L, "Taco 2", 12.0, 12);

        when(mockTacoService.findTacosByIdIn(List.of(taco1.getId(), taco2.getId())))
                .thenReturn(Map.of(taco1.getId(), taco1, taco2.getId(), taco2));
        when(mockToppingService.findToppingsByIdIn(List.of(topping1.getId(), topping2.getId(), topping1.getId())))
                .thenReturn(Map.of(topping1.getId(), topping1, topping2.getId(), topping2));
        when(mockOrderRepository.save(expectedOrder)).thenReturn(savedOrder);

        // When
        Long result = orderService.orderTacos(request);

        // Then
        assertEquals(savedOrder.getId(), result);
        verify(mockTacoService).findTacosByIdIn(List.of(taco1.getId(), taco2.getId()));
        verify(mockToppingService).findToppingsByIdIn(List.of(topping1.getId(), topping2.getId(), topping1.getId()));
        verify(mockOrderRepository).save(expectedOrder);
        verify(mockTacoService).save(savedTaco1);
        verify(mockTacoService).save(savedTaco2);
    }

    @Test
    @DisplayName("Should throw NotEnoughAmountException when not enough tacos are available")
    void notEnoughTacos() {
        // Given
        OrderItemRequestDto orderItemRequest = OrderItemRequestDto.builder()
                .tacoId(1L)
                .toppingIds(List.of(1L))
                .count(12)
                .build(); // Requesting more than available
        OrderRequestDto request = OrderRequestDto.builder()
                .orderItems(List.of(orderItemRequest))
                .build();

        Taco taco = new Taco(1L, "Taco 1", 10.0, 10);
        when(mockTacoService.findTacosByIdIn(List.of(taco.getId()))).thenReturn(Map.of(taco.getId(), taco));

        // When & Then
        assertThrows(NotEnoughAmountException.class, () -> orderService.orderTacos(request));
        verify(mockTacoService).findTacosByIdIn(List.of(taco.getId()));
        verify(mockToppingService).findToppingsByIdIn(List.of(1L));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when taco is not found")
    void tacoNotFound() {
        // Given
        OrderItemRequestDto orderItemRequest = OrderItemRequestDto.builder()
                .tacoId(99L)
                .toppingIds(List.of(1L))
                .count(2)
                .build(); // Taco ID 99 doesn't exist
        OrderRequestDto request = OrderRequestDto.builder()
                .orderItems(List.of(orderItemRequest))
                .build();

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> orderService.orderTacos(request));
        verify(mockTacoService).findTacosByIdIn(List.of(99L));
        verify(mockToppingService).findToppingsByIdIn(List.of(1L));
    }
}
