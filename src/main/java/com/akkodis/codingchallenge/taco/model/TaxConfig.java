package com.akkodis.codingchallenge.taco.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxConfig {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate effectiveDate;
    private double stateTaxRate;
    private double federalTaxRate;
}