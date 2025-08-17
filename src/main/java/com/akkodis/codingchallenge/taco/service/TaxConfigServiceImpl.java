package com.akkodis.codingchallenge.taco.service;

import com.akkodis.codingchallenge.taco.dao.TaxConfigRepository;
import com.akkodis.codingchallenge.taco.model.TaxConfig;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaxConfigServiceImpl implements TaxConfigService {

    private final TaxConfigRepository taxConfigRepository;

    @Override
    public TaxConfig findCurrentTaxConfig() {
        // Needed to be able to change tex rates without maintenance.
        // Example: We can add new settings for the next year, and they will apply for all new bills from January 1th.
        // Adding new settings is out of scope. Time Zones support is out of scope.
        return taxConfigRepository.findLastTaxConfigForCurrentDate(LocalDate.now())
                .orElseThrow(() -> new EntityNotFoundException("Tax configuration not found for the current date"));
    }
}