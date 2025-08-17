package com.akkodis.codingchallenge.taco.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {
    @Id
    @GeneratedValue
    private Long id;
    private long orderId;
    private double totalWithoutTax;
    private double stateTax;
    private double federalTax;
    private double totalWithTax;
}