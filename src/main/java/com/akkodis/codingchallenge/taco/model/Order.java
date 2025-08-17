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
@Table(name = "tako_order") // Specify a different table name
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany
    private List<OrderItem> orderItems = new ArrayList<>();
}