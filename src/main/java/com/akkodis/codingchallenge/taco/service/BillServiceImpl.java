package com.akkodis.codingchallenge.taco.service;

import com.akkodis.codingchallenge.taco.dao.BillRepository;
import com.akkodis.codingchallenge.taco.dao.OrderRepository;
import com.akkodis.codingchallenge.taco.model.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final OrderRepository orderRepository;
    private final BillRepository billRepository;
    private final TaxConfigService taxConfigService;

    @Override
    public Bill calculateBill(Long orderId) {
        //look for the order for idempotency
        return billRepository.findByOrderId(orderId)
                .orElseGet(() -> createNewBill(orderId));
    }

    private Bill createNewBill(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found by orderId=%s".formatted(orderId)));

        TaxConfig taxConfig = taxConfigService.findCurrentTaxConfig();

        double totalWithoutTax = calculateTotalWithoutTax(order);

        Bill bill = getBill(orderId, totalWithoutTax, taxConfig.getStateTaxRate(), taxConfig.getFederalTaxRate());
        return billRepository.save(bill);
    }

    private Bill getBill(Long orderId, double totalWithoutTax, double stateTaxRate, double federalTaxRate) {
        double totalStateTax = totalWithoutTax * stateTaxRate;
        double totalFederalTax = totalWithoutTax * federalTaxRate;
        double totalWithTax = roundToCents(totalWithoutTax + totalStateTax + totalFederalTax);

        Bill bill = new Bill();
        bill.setOrderId(orderId);
        bill.setTotalWithoutTax(totalWithoutTax);
        bill.setStateTax(stateTaxRate);
        bill.setFederalTax(federalTaxRate);
        bill.setTotalWithTax(totalWithTax);
        return bill;
    }

    private double calculateTotalWithoutTax(Order order) {
        double totalCost = 0d;
        for (OrderItem orderItem : order.getOrderItems()) {
            totalCost += orderItem.getTaco().getCost();
            for (Topping topping : orderItem.getToppings()) {
                totalCost += topping.getCost();
            }
        }
        return roundToCents(totalCost);
    }

    private double roundToCents(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}