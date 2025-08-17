package com.akkodis.codingchallenge.taco.web.dto.response;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.hateoas.RepresentationModel;

@Value
@Builder
@Jacksonized
public class BillDto extends RepresentationModel<BillDto> {
    Long orderId;
    double totalWithoutTax;
    double stateTax;
    double federalTax;
    double totalWithTax;
}