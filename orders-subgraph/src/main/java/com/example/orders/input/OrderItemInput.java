package com.example.orders.input;

import lombok.Data;

@Data
public class OrderItemInput {
    private String productId;
    private Integer quantity;
    private Double price;
}
