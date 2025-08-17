package com.akkodis.codingchallenge.taco.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Taco taco;
    private int quantity;
    @ManyToMany
    private List<Topping> toppings = new ArrayList<>();
}