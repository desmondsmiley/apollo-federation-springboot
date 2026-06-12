package com.example.orders.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String id;
    private OrderStatus status;
    private Double totalPrice;
    private List<OrderItem> items;
    private User user;
    private String createdAt;
}
