package com.akkodis.codingchallenge.taco.service;

import com.akkodis.codingchallenge.taco.dao.TaxConfigRepository;
import com.akkodis.codingchallenge.taco.model.TaxConfig;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxConfigServiceImplTest {

    @Mock
    private TaxConfigRepository mockTaxConfigRepository;

    @InjectMocks
    private TaxConfigServiceImpl taxConfigService;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(mockTaxConfigRepository);
    }

    @Test
    @DisplayName("Should return current tax configuration")
    void findCurrentTaxConfigReturnsData() {
        // Given
        LocalDate currentDate = LocalDate.now();
        TaxConfig taxConfig = new TaxConfig(1L, currentDate, 0.1, 0.05);

        when(mockTaxConfigRepository.findLastTaxConfigForCurrentDate(currentDate)).thenReturn(Optional.of(taxConfig));

        // When
        TaxConfig result = taxConfigService.findCurrentTaxConfig();

        // Then
        assertEquals(taxConfig, result);
        verify(mockTaxConfigRepository).findLastTaxConfigForCurrentDate(currentDate);
    }

    @Test
    @DisplayName("Should throw exception when tax configuration is not found")
    void findCurrentTaxConfigThrowsException() {
        // Given
        LocalDate currentDate = LocalDate.now();
        when(mockTaxConfigRepository.findLastTaxConfigForCurrentDate(currentDate)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> taxConfigService.findCurrentTaxConfig());
        verify(mockTaxConfigRepository).findLastTaxConfigForCurrentDate(currentDate);
    }
}
