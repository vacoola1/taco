package com.akkodis.codingchallenge.taco.web.dto.response;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.hateoas.RepresentationModel;

@Value
@Builder
@Jacksonized
public class ToppingDto extends RepresentationModel<ToppingDto> {
    Long id;
    String name;
    double cost;
}