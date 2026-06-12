package com.example.products.service;

import com.example.products.model.Product;
import com.example.products.model.Review;
import com.example.products.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final Map<String, Product> products = new ConcurrentHashMap<>();
    private final AtomicInteger productIdCounter = new AtomicInteger(4);
    private final AtomicInteger reviewIdCounter = new AtomicInteger(5);

    public ProductService() {
        products.put("1", new Product("1",
            "Laptop Pro 15",
            "High-performance laptop with 15-inch display, 16GB RAM, and 512GB SSD",
            1299.99, true,
            new ArrayList<>(Arrays.asList(
                new Review("1", 5, "Amazing laptop! Best purchase ever. Super fast and great display.", new User("1")),
                new Review("2", 4, "Great performance, slightly expensive but worth it.", new User("2"))
            ))));

        products.put("2", new Product("2",
            "Wireless Ergonomic Mouse",
            "Ergonomic wireless mouse with adjustable DPI and long battery life",
            49.99, true,
            new ArrayList<>(Arrays.asList(
                new Review("3", 3, "Decent mouse, comfortable to use but nothing extraordinary.", new User("3"))
            ))));

        products.put("3", new Product("3",
            "Mechanical Keyboard RGB",
            "Compact RGB mechanical keyboard with tactile blue switches",
            129.99, false,
            new ArrayList<>(Arrays.asList(
                new Review("4", 5, "Best keyboard I have ever used! The clicky feel is perfect.", new User("1"))
            ))));
    }

    public Product findById(String id) {
        return products.get(id);
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public List<Product> findByStock(boolean inStock) {
        return products.values().stream()
            .filter(p -> p.getInStock() == inStock)
            .collect(Collectors.toList());
    }

    public Product create(String name, String description, Double price, Boolean inStock) {
        String id = String.valueOf(productIdCounter.getAndIncrement());
        Product product = new Product(id, name, description, price, inStock, new ArrayList<>());
        products.put(id, product);
        return product;
    }

    public Product update(String id, String name, String description, Double price, Boolean inStock) {
        Product existing = products.get(id);
        if (existing == null) return null;
        if (name != null) existing.setName(name);
        if (description != null) existing.setDescription(description);
        if (price != null) existing.setPrice(price);
        if (inStock != null) existing.setInStock(inStock);
        return existing;
    }

    public boolean delete(String id) {
        return products.remove(id) != null;
    }

    public Review addReview(String productId, Integer rating, String comment, String authorId) {
        Product product = products.get(productId);
        if (product == null) return null;
        String reviewId = String.valueOf(reviewIdCounter.getAndIncrement());
        Review review = new Review(reviewId, rating, comment, new User(authorId));
        product.getReviews().add(review);
        return review;
    }
}
