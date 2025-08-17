package com.akkodis.codingchallenge.taco.service;

import com.akkodis.codingchallenge.taco.web.dto.request.OrderItemRequestDto;
import com.akkodis.codingchallenge.taco.web.dto.request.OrderRequestDto;
import com.akkodis.codingchallenge.taco.dao.OrderRepository;
import com.akkodis.codingchallenge.taco.model.Order;
import com.akkodis.codingchallenge.taco.model.OrderItem;
import com.akkodis.codingchallenge.taco.model.Taco;
import com.akkodis.codingchallenge.taco.model.Topping;
import com.akkodis.codingchallenge.taco.service.exceptions.NotEnoughAmountException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TacoService tacoService;
    private final ToppingService toppingService;

    @Override
    @Transactional
    public Long orderTacos(OrderRequestDto orderRequest) {
        Map<Long, Taco> tacoMap = getTacoMap(orderRequest);
        Map<Long, Topping> toppingMap = getTopingMap(orderRequest);

        Order order = new Order();

        for (OrderItemRequestDto orderItemRequest : orderRequest.getOrderItems()) {
            Taco taco = getTaco(orderItemRequest.getTacoId(), tacoMap);

            if (taco.getNumberAvailable() < orderItemRequest.getCount()) {
                throw new NotEnoughAmountException("Not enough amount of Taco. Needed=%s, Available=%s"
                        .formatted(orderItemRequest.getCount(), taco.getNumberAvailable()));
            }

            List<Topping> toppings = getToppings(orderItemRequest.getToppingIds(), toppingMap);

            OrderItem orderItem = new OrderItem();
            orderItem.setTaco(taco);
            orderItem.setToppings(toppings);
            orderItem.setQuantity(orderItemRequest.getCount());

            order.getOrderItems().add(orderItem);

            taco.setNumberAvailable(taco.getNumberAvailable() - orderItemRequest.getCount());
            tacoService.save(taco);
        }

        return orderRepository.save(order).getId();
    }

    private static List<Topping> getToppings(List<Long> toppingIds, Map<Long, Topping> toppingMap) {
        return toppingIds.stream()
                .map(toppingId -> {
                    if (!toppingMap.containsKey(toppingId)) {
                        throw new EntityNotFoundException("Topping not found by Id: %s".formatted(toppingId));
                    }
                    return toppingMap.get(toppingId);
                }).toList();
    }

    private static Taco getTaco(Long tacoId, Map<Long, Taco> tacoMap) {
        if (!tacoMap.containsKey(tacoId)) {
            throw new EntityNotFoundException("Taco not found by Id: %s".formatted(tacoId));
        }
        return tacoMap.get(tacoId);
    }

    private Map<Long, Taco> getTacoMap(OrderRequestDto orderRequest) {
        List<Long> takoIds = orderRequest.getOrderItems()
                .stream()
                .map(OrderItemRequestDto::getTacoId)
                .toList();
        return tacoService.findTacosByIdIn(takoIds);
    }

    private Map<Long, Topping> getTopingMap(OrderRequestDto orderRequest) {
        List<Long> toppingIds = orderRequest.getOrderItems()
                .stream()
                .flatMap(item -> item.getToppingIds().stream())
                .toList();

        return toppingService.findToppingsByIdIn(toppingIds);
    }
}
