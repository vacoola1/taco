package com.akkodis.codingchallenge.taco.web.dto.assembler;

import com.akkodis.codingchallenge.taco.model.Bill;
import com.akkodis.codingchallenge.taco.web.api.BillController;
import com.akkodis.codingchallenge.taco.web.dto.response.BillDto;
import com.akkodis.codingchallenge.taco.web.dto.mapper.BillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class BillDtoAssembler extends RepresentationModelAssemblerSupport<Bill, BillDto> {
    private final BillMapper mapper;

    @Autowired
    public BillDtoAssembler(BillMapper mapper) {
        super(BillController.class, BillDto.class);
        this.mapper = mapper;
    }

    @Override
    public BillDto toModel(Bill entity) {
        return mapper.map(entity);
    }
}