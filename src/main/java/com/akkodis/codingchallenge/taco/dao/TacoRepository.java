package com.akkodis.codingchallenge.taco.dao;

import com.akkodis.codingchallenge.taco.model.Taco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface TacoRepository extends JpaRepository<Taco, Long> {
    List<Taco> findTacosByIdIn(List<Long> tacoIds);
}