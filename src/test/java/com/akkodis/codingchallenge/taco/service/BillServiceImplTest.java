package com.akkodis.codingchallenge.taco.service;


import com.akkodis.codingchallenge.taco.dao.BillRepository;
import com.akkodis.codingchallenge.taco.dao.OrderRepository;
import com.akkodis.codingchallenge.taco.model.*;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillServiceImplTest {

    @Mock
    private OrderRepository mockOrderRepository;

    @Mock
    private BillRepository mockBillRepository;

    @Mock
    private TaxConfigService mockTaxConfigService;

    @InjectMocks
    private BillServiceImpl billService;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(mockOrderRepository, mockBillRepository, mockTaxConfigService);
    }

    @Test
    @DisplayName("Should calculate a new bill when one doesn't exist")
    void calculateBillNew() {
        // Given
        Long orderId = 1L;
        Order order = new Order();
        order.setOrderItems(List.of(
                new OrderItem(1L, new Taco(1L, "Taco", 10.0, 10), 2,
                        List.of(
                                new Topping(1L, "Topping", 1.0),
                                new Topping(2L, "Topping", 2.0)
                        )
                ),
                new OrderItem(1L, new Taco(1L, "Taco", 13.0, 10), 5,
                        List.of(
                                new Topping(1L, "Topping", 1.0),
                                new Topping(3L, "Topping", 3.0)
                        )
                )
        ));
        TaxConfig taxConfig = new TaxConfig(1L, LocalDate.of(2022, 1, 1),
                0.05, 0.10);  // 5% state tax, 10% federal tax

        Bill bill = new Bill();
        bill.setOrderId(1L);
        bill.setTotalWithoutTax(30);
        bill.setStateTax(0.05);
        bill.setFederalTax(0.10);
        bill.setTotalWithTax(34.5);

        Bill savedBill = new Bill();
        savedBill.setId(1L);
        savedBill.setOrderId(1L);
        savedBill.setTotalWithoutTax(30);
        savedBill.setStateTax(0.05);
        savedBill.setFederalTax(0.10);
        savedBill.setTotalWithTax(34.5);

        when(mockBillRepository.findByOrderId(orderId)).thenReturn(Optional.empty());
        when(mockOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(mockTaxConfigService.findCurrentTaxConfig()).thenReturn(taxConfig);
        when(mockBillRepository.save(bill)).thenReturn(savedBill);

        // When
        Bill result = billService.calculateBill(orderId);

        // Then
        assertEquals(savedBill, result);
        verify(mockBillRepository).findByOrderId(orderId);
        verify(mockOrderRepository).findById(orderId);
        verify(mockTaxConfigService).findCurrentTaxConfig();
        verify(mockBillRepository).save(bill);
    }

    @Test
    @DisplayName("Should return existing bill if one exists")
    void calculateBillExisting() {
        // Given
        Long orderId = 1L;
        Bill existingBill = new Bill(1L, orderId, 100.0, 0.05, 0.10, 115.0);

        when(mockBillRepository.findByOrderId(orderId)).thenReturn(Optional.of(existingBill));

        // When
        Bill result = billService.calculateBill(orderId);

        // Then
        assertEquals(existingBill, result);
        verify(mockBillRepository).findByOrderId(orderId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException if order doesn't exist")
    void calculateBillOrderNotFound() {
        // Given
        Long orderId = 1L;

        when(mockBillRepository.findByOrderId(orderId)).thenReturn(Optional.empty());
        when(mockOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> billService.calculateBill(orderId));
        verify(mockBillRepository).findByOrderId(orderId);
        verify(mockOrderRepository).findById(orderId);
    }
}