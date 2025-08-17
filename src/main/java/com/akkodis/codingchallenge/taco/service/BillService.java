package com.akkodis.codingchallenge.taco.service;

import com.akkodis.codingchallenge.taco.model.Bill;

public interface BillService {
    Bill calculateBill(Long orderId);
}