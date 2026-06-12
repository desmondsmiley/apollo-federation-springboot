package com.example.orders.datafetcher;

import com.example.orders.model.Order;
import com.example.orders.service.OrderService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsEntityFetcher;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@DgsComponent
@RequiredArgsConstructor
public class OrderEntityFetcher {

    private final OrderService orderService;

    @DgsEntityFetcher(name = "Order")
    public Order fetchOrderById(Map<String, Object> values) {
        String id = (String) values.get("id");
        return orderService.findById(id);
    }
}
