package com.akkodis.codingchallenge.taco.web.dto.mapper;

import com.akkodis.codingchallenge.taco.web.dto.response.BillDto;
import com.akkodis.codingchallenge.taco.model.Bill;
import org.mapstruct.Mapper;

@Mapper
public interface BillMapper {
    BillDto map(Bill bill);
}