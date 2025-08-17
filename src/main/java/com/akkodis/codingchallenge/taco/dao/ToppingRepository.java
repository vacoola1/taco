package com.akkodis.codingchallenge.taco.dao;

import com.akkodis.codingchallenge.taco.model.Topping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToppingRepository extends JpaRepository<Topping, Long> {
    List<Topping> findToppingsByIdIn(List<Long> ids);
}