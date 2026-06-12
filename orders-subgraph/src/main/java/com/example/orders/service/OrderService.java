package com.example.orders.service;

import com.example.orders.input.CreateOrderInput;
import com.example.orders.model.Order;
import com.example.orders.model.OrderItem;
import com.example.orders.model.OrderStatus;
import com.example.orders.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(4);

    public OrderService() {
        orders.put("1", new Order("1", OrderStatus.DELIVERED, 1349.98,
            new ArrayList<>(Arrays.asList(
                new OrderItem("1", 1, 1299.99),
                new OrderItem("2", 1, 49.99)
            )),
            new User("1"), "2026-05-10T10:00:00Z"));

        orders.put("2", new Order("2", OrderStatus.PROCESSING, 129.99,
            new ArrayList<>(Arrays.asList(
                new OrderItem("3", 1, 129.99)
            )),
            new User("2"), "2026-06-01T14:30:00Z"));

        orders.put("3", new Order("3", OrderStatus.PENDING, 99.98,
            new ArrayList<>(Arrays.asList(
                new OrderItem("2", 2, 49.99)
            )),
            new User("1"), "2026-06-11T09:15:00Z"));
    }

    public Order findById(String id) {
        return orders.get(id);
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    public List<Order> findByUserId(String userId) {
        return orders.values().stream()
            .filter(o -> o.getUser().getId().equals(userId))
            .collect(Collectors.toList());
    }

    public Order create(CreateOrderInput input) {
        String id = String.valueOf(idCounter.getAndIncrement());
        List<OrderItem> items = input.getItems().stream()
            .map(i -> new OrderItem(i.getProductId(), i.getQuantity(), i.getPrice()))
            .collect(Collectors.toList());
        double total = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        Order order = new Order(id, OrderStatus.PENDING, total, items,
            new User(input.getUserId()), Instant.now().toString());
        orders.put(id, order);
        return order;
    }

    public Order updateStatus(String id, OrderStatus status) {
        Order order = orders.get(id);
        if (order == null) return null;
        order.setStatus(status);
        return order;
    }

    public boolean cancel(String id) {
        Order order = orders.get(id);
        if (order == null) return false;
        order.setStatus(OrderStatus.CANCELLED);
        return true;
    }
}
