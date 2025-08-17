package com.akkodis.codingchallenge.taco.web.dto.mapper;

import com.akkodis.codingchallenge.taco.web.dto.response.TacoDto;
import com.akkodis.codingchallenge.taco.model.Taco;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TacoMapper {
    TacoDto map(Taco taco);
    List<TacoDto> map(List<Taco> tacos);
}