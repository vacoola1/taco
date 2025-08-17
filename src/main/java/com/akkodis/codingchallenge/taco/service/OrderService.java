package com.akkodis.codingchallenge.taco.service;

import com.akkodis.codingchallenge.taco.web.dto.request.OrderRequestDto;

public interface OrderService {
    Long orderTacos(OrderRequestDto orderRequestDto);
}