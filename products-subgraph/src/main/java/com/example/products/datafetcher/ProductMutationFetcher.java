package com.example.products.datafetcher;

import com.example.products.input.AddReviewInput;
import com.example.products.input.CreateProductInput;
import com.example.products.input.UpdateProductInput;
import com.example.products.model.Product;
import com.example.products.model.Review;
import com.example.products.service.ProductService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class ProductMutationFetcher {

    @Autowired
    private ProductService productService;

    @DgsMutation
    public Product createProduct(@InputArgument CreateProductInput input) {
        return productService.create(
            input.getName(),
            input.getDescription(),
            input.getPrice(),
            input.getInStock() != null ? input.getInStock() : true
        );
    }

    @DgsMutation
    public Product updateProduct(@InputArgument String id, @InputArgument UpdateProductInput input) {
        return productService.update(
            id,
            input.getName(),
            input.getDescription(),
            input.getPrice(),
            input.getInStock()
        );
    }

    @DgsMutation
    public boolean deleteProduct(@InputArgument String id) {
        return productService.delete(id);
    }

    @DgsMutation
    public Review addReview(@InputArgument AddReviewInput input) {
        return productService.addReview(
            input.getProductId(),
            input.getRating(),
            input.getComment(),
            input.getAuthorId()
        );
    }
}
