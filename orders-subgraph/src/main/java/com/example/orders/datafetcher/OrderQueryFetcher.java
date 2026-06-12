package com.example.orders.datafetcher;

import com.example.orders.model.Order;
import com.example.orders.service.OrderService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class OrderQueryFetcher {

    private final OrderService orderService;

    @DgsQuery
    public Order order(@InputArgument String id) {
        return orderService.findById(id);
    }

    @DgsQuery
    public List<Order> orders() {
        return orderService.findAll();
    }

    @DgsQuery
    public List<Order> ordersByUser(@InputArgument String userId) {
        return orderService.findByUserId(userId);
    }
}
