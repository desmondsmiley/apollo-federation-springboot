package com.example.users.datafetcher;

import com.example.users.model.User;
import com.example.users.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class UserQueryFetcher {

    private final UserService userService;

    @DgsQuery
    public User user(@InputArgument String id) {
        return userService.findById(id);
    }

    @DgsQuery
    public List<User> users() {
        return userService.findAll();
    }
}
