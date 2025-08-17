package com.akkodis.codingchallenge.taco.web.dto.mapper;

import com.akkodis.codingchallenge.taco.web.dto.response.ToppingDto;
import com.akkodis.codingchallenge.taco.model.Topping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ToppingMapper {
    ToppingDto map(Topping topping);
    List<ToppingDto> map(List<Topping> toppings);
}