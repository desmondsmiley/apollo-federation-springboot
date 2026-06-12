package com.example.products.datafetcher;

import com.example.products.model.Product;
import com.example.products.service.ProductService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DgsComponent
public class ProductQueryFetcher {

    @Autowired
    private ProductService productService;

    @DgsQuery
    public Product product(@InputArgument String id) {
        return productService.findById(id);
    }

    @DgsQuery
    public List<Product> products() {
        return productService.findAll();
    }

    @DgsQuery
    public List<Product> productsByStock(@InputArgument boolean inStock) {
        return productService.findByStock(inStock);
    }
}
