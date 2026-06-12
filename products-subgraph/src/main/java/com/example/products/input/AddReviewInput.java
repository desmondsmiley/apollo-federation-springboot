package com.example.products.input;

import lombok.Data;

@Data
public class AddReviewInput {
    private String productId;
    private Integer rating;
    private String comment;
    private String authorId;
}
