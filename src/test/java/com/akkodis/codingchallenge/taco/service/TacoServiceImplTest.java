package com.akkodis.codingchallenge.taco.service;

import com.akkodis.codingchallenge.taco.dao.TacoRepository;
import com.akkodis.codingchallenge.taco.model.Taco;
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
class TacoServiceImplTest {

    @Mock
    private TacoRepository mockTacoRepository;

    @InjectMocks
    private TacoServiceImpl tacoService;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(mockTacoRepository);
    }

    @Test
    @DisplayName("Should return all tacos when there are tacos available")
    void findAllTacosReturnsData() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Taco taco1 = new Taco(1L, "Taco 1", 5.0, 10);
        Taco taco2 = new Taco(2L, "Taco 2", 6.0, 8);

        when(mockTacoRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(taco1, taco2)));

        // When
        Page<Taco> result = tacoService.findAllTacos(pageable);

        // Then
        assertEquals(new PageImpl<>(List.of(taco1, taco2)), result);
        verify(mockTacoRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return map of tacos by their IDs")
    void findTacosByIdInReturnsData() {
        // Given
        List<Long> ids = List.of(1L, 2L);
        Taco taco1 = new Taco(1L, "Taco 1", 5.0, 10);
        Taco taco2 = new Taco(2L, "Taco 2", 6.0, 8);

        when(mockTacoRepository.findTacosByIdIn(ids)).thenReturn(List.of(taco1, taco2));

        // When
        Map<Long, Taco> result = tacoService.findTacosByIdIn(ids);

        // Then
        assertEquals(Map.of(1L, taco1, 2L, taco2), result);
        verify(mockTacoRepository).findTacosByIdIn(ids);
    }

    @Test
    @DisplayName("Should save and return the saved taco")
    void saveTacoReturnsData() {
        // Given
        Taco taco = new Taco(null, "Taco 1", 5.0, 10);
        Taco savedTaco = new Taco(1L, "Taco 1", 5.0, 10);

        when(mockTacoRepository.save(taco)).thenReturn(savedTaco);

        // When
        Taco result = tacoService.save(taco);

        // Then
        assertEquals(savedTaco, result);
        verify(mockTacoRepository).save(taco);
    }
}
