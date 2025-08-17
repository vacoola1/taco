package com.akkodis.codingchallenge.taco.web.dto.request;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;


@Value
@Builder
@Jacksonized
public class OrderItemRequestDto {
    Long tacoId;
    @Singular
    List<Long> toppingIds;
    int count;
}
