package com.akkodis.codingchallenge.taco.service;

import com.akkodis.codingchallenge.taco.model.Taco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface TacoService {
    Page<Taco> findAllTacos(Pageable pageable);
    Map<Long, Taco> findTacosByIdIn(List<Long> ids);
    Taco save(Taco taco);
}