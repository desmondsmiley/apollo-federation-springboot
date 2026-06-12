package com.example.products.input;

import lombok.Data;

@Data
public class UpdateProductInput {
    private String name;
    private String description;
    private Double price;
    private Boolean inStock;
}
