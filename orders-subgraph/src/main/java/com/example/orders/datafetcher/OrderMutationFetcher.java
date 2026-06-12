package com.example.orders.datafetcher;

import com.example.orders.input.CreateOrderInput;
import com.example.orders.model.Order;
import com.example.orders.model.OrderStatus;
import com.example.orders.service.OrderService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;

@DgsComponent
@RequiredArgsConstructor
public class OrderMutationFetcher {

    private final OrderService orderService;

    @DgsMutation
    public Order createOrder(@InputArgument CreateOrderInput input) {
        return orderService.create(input);
    }

    @DgsMutation
    public Order updateOrderStatus(@InputArgument String id, @InputArgument OrderStatus status) {
        return orderService.updateStatus(id, status);
    }

    @DgsMutation
    public boolean cancelOrder(@InputArgument String id) {
        return orderService.cancel(id);
    }
}
