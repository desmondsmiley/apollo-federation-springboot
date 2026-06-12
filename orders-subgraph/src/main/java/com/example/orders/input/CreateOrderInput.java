package com.example.orders.input;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderInput {
    private String userId;
    private List<OrderItemInput> items;
}
