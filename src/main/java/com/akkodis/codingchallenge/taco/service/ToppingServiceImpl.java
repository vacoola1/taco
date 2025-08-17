package com.akkodis.codingchallenge.taco.service;

import com.akkodis.codingchallenge.taco.dao.ToppingRepository;
import com.akkodis.codingchallenge.taco.model.Topping;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToppingServiceImpl implements ToppingService {

    private final ToppingRepository toppingRepository;

    @Override
    public Page<Topping> findAllToppings(Pageable pageable) {
        return toppingRepository.findAll(pageable);
    }

    @Override
    public Map<Long, Topping> findToppingsByIdIn(List<Long> toppingIds) {
        return toppingRepository.findToppingsByIdIn(toppingIds).stream()
                .collect(Collectors.toMap(Topping::getId, topping -> topping));
    }
}