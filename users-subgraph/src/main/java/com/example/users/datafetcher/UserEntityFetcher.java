package com.example.users.datafetcher;

import com.example.users.model.User;
import com.example.users.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsEntityFetcher;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Handles Apollo Federation entity resolution.
 *
 * When the Products subgraph returns a Review with author: {id: "1"},
 * the Apollo Router calls the Users subgraph with:
 *   _entities(representations: [{__typename: "User", id: "1"}])
 *
 * This fetcher handles that request and returns the full User object.
 */
@DgsComponent
@RequiredArgsConstructor
public class UserEntityFetcher {

    private final UserService userService;

    @DgsEntityFetcher(name = "User")
    public User fetchUserById(Map<String, Object> values) {
        String id = (String) values.get("id");
        return userService.findById(id);
    }
}
