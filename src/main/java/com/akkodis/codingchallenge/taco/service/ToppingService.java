package com.akkodis.codingchallenge.taco.service;

import com.akkodis.codingchallenge.taco.model.Topping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


public interface ToppingService {
    Page<Topping> findAllToppings(Pageable pageable);

    Map<Long, Topping> findToppingsByIdIn(List<Long> toppingIds);
}