package com.akkodis.codingchallenge.taco.dao;

import com.akkodis.codingchallenge.taco.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByOrderId(Long orderId);
}