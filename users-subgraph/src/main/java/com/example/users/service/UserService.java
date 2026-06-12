package com.example.users.service;

import com.example.users.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService {

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(4);

    public UserService() {
        users.put("1", new User("1", "Alice Johnson", "alice@example.com", "alice"));
        users.put("2", new User("2", "Bob Smith", "bob@example.com", "bob"));
        users.put("3", new User("3", "Charlie Brown", "charlie@example.com", "charlie"));
    }

    public User findById(String id) {
        return users.get(id);
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User create(String name, String email, String username) {
        String id = String.valueOf(idCounter.getAndIncrement());
        User user = new User(id, name, email, username);
        users.put(id, user);
        return user;
    }

    public User update(String id, String name, String email, String username) {
        User existing = users.get(id);
        if (existing == null) return null;
        if (name != null) existing.setName(name);
        if (email != null) existing.setEmail(email);
        if (username != null) existing.setUsername(username);
        return existing;
    }

    public boolean delete(String id) {
        return users.remove(id) != null;
    }
}
