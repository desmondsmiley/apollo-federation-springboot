package com.example.products.input;

import lombok.Data;

@Data
public class CreateProductInput {
    private String name;
    private String description;
    private Double price;
    private Boolean inStock = true;
}
