package com.akkodis.codingchallenge.taco.service;

import com.akkodis.codingchallenge.taco.dao.TacoRepository;
import com.akkodis.codingchallenge.taco.model.Taco;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TacoServiceImpl implements TacoService {

    private final TacoRepository tacoRepository;

    @Override
    public Page<Taco> findAllTacos(Pageable pageable) {
        return tacoRepository.findAll(pageable);
    }

    @Override
    public Map<Long, Taco> findTacosByIdIn(List<Long> takoIds) {
        return tacoRepository.findTacosByIdIn(takoIds).stream()
                .collect(Collectors.toMap(Taco::getId, taco -> taco));
    }

    @Override
    public Taco save(Taco taco) {
        return tacoRepository.save(taco);
    }
}