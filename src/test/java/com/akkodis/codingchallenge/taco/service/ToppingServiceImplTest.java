package com.akkodis.codingchallenge.taco.service;

import com.akkodis.codingchallenge.taco.dao.ToppingRepository;
import com.akkodis.codingchallenge.taco.model.Topping;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToppingServiceImplTest {

    @Mock
    private ToppingRepository mockToppingRepository;

    @InjectMocks
    private ToppingServiceImpl toppingService;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(mockToppingRepository);
    }

    @Test
    @DisplayName("Should return toppings when there are toppings available")
    void findAllToppingsReturnsData() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Topping topping1 = new Topping(1L, "Topping 1", 1.0);
        Topping topping2 = new Topping(2L, "Topping 2", 2.0);

        when(mockToppingRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(topping1, topping2)));

        // When
        Page<Topping> result = toppingService.findAllToppings(pageable);

        // Then
        assertEquals(new PageImpl<>(List.of(topping1, topping2)), result);
        verify(mockToppingRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return map of toppings by their IDs")
    void findTacosByIdInReturnsData() {
        // Given
        List<Long> ids = List.of(1L, 2L);
        Topping topping1 = new Topping(1L, "Topping 1", 1.0);
        Topping topping2 = new Topping(2L, "Topping 2", 2.0);

        when(mockToppingRepository.findToppingsByIdIn(ids)).thenReturn(List.of(topping1, topping2));

        // When
        Map<Long, Topping> result = toppingService.findToppingsByIdIn(ids);

        // Then
        assertEquals(Map.of(1L, topping1, 2L, topping2), result);
        verify(mockToppingRepository).findToppingsByIdIn(ids);
    }
}