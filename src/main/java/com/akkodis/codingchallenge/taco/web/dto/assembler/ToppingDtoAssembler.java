package com.akkodis.codingchallenge.taco.web.dto.assembler;

import com.akkodis.codingchallenge.taco.model.Topping;
import com.akkodis.codingchallenge.taco.web.api.ToppingController;
import com.akkodis.codingchallenge.taco.web.dto.response.ToppingDto;
import com.akkodis.codingchallenge.taco.web.dto.mapper.ToppingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ToppingDtoAssembler extends RepresentationModelAssemblerSupport<Topping, ToppingDto> {
    private final ToppingMapper mapper;

    @Autowired
    public ToppingDtoAssembler(ToppingMapper mapper) {
        super(ToppingController.class, ToppingDto.class);
        this.mapper = mapper;
    }

    @Override
    public ToppingDto toModel(Topping entity) {
        return mapper.map(entity);
    }
}