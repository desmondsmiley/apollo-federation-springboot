package com.example.users.datafetcher;

import com.example.users.model.User;
import com.example.users.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DgsComponent
public class UserQueryFetcher {

    @Autowired
    private UserService userService;

    @DgsQuery
    public User user(@InputArgument String id) {
        return userService.findById(id);
    }

    @DgsQuery
    public List<User> users() {
        return userService.findAll();
    }
}
