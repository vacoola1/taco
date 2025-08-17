package com.akkodis.codingchallenge.taco.dao;

import com.akkodis.codingchallenge.taco.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}