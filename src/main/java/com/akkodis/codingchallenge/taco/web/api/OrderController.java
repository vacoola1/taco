package com.akkodis.codingchallenge.taco.web.api;

import com.akkodis.codingchallenge.taco.web.dto.request.OrderRequestDto;
import com.akkodis.codingchallenge.taco.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> orderTacos(@RequestBody OrderRequestDto orderRequestDto) {
        Long orderId = orderService.orderTacos(orderRequestDto);
        return ResponseEntity.ok(orderId);
    }
}