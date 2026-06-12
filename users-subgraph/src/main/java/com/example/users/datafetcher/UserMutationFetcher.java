package com.example.users.datafetcher;

import com.example.users.input.CreateUserInput;
import com.example.users.input.UpdateUserInput;
import com.example.users.model.User;
import com.example.users.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class UserMutationFetcher {

    @Autowired
    private UserService userService;

    @DgsMutation
    public User createUser(@InputArgument CreateUserInput input) {
        return userService.create(input.getName(), input.getEmail(), input.getUsername());
    }

    @DgsMutation
    public User updateUser(@InputArgument String id, @InputArgument UpdateUserInput input) {
        return userService.update(id, input.getName(), input.getEmail(), input.getUsername());
    }

    @DgsMutation
    public boolean deleteUser(@InputArgument String id) {
        return userService.delete(id);
    }
}
