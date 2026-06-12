package com.example.products.datafetcher;

import com.example.products.model.Product;
import com.example.products.service.ProductService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsEntityFetcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Handles Apollo Federation entity resolution for the Product type.
 *
 * When another subgraph references a Product entity, the router
 * sends a batch request to _entities with the product id, and
 * this fetcher returns the full Product object.
 */
@DgsComponent
public class ProductEntityFetcher {

    @Autowired
    private ProductService productService;

    @DgsEntityFetcher(name = "Product")
    public Product fetchProductById(Map<String, Object> values) {
        String id = (String) values.get("id");
        return productService.findById(id);
    }
}
