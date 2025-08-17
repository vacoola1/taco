package com.akkodis.codingchallenge.taco.web.dto.assembler;

import com.akkodis.codingchallenge.taco.model.Taco;
import com.akkodis.codingchallenge.taco.web.api.TacoController;
import com.akkodis.codingchallenge.taco.web.dto.response.TacoDto;
import com.akkodis.codingchallenge.taco.web.dto.mapper.TacoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class TacoDtoAssembler extends RepresentationModelAssemblerSupport<Taco, TacoDto> {
    private final TacoMapper mapper;

    @Autowired
    public TacoDtoAssembler(TacoMapper mapper) {
        super(TacoController.class, TacoDto.class);
        this.mapper = mapper;
    }

    @Override
    public TacoDto toModel(Taco entity) {
        return mapper.map(entity);
    }
}