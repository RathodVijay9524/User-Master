package com.vijay.User_Master.controller;

import com.vijay.User_Master.Helper.ExceptionUtil;
import com.vijay.User_Master.dto.UserRequest;
import com.vijay.User_Master.dto.UserResponse;
import com.vijay.User_Master.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;

    // Endpoint to create a new user
    @PostMapping
    public CompletableFuture<ResponseEntity<?>> createUser(@RequestBody UserRequest userRequest) {
        log.info("Received request to create user with username: {}", userRequest.getUsername());

        // Call the service to create the user asynchronously
        return userService.create(userRequest)
                .thenApply(userResponse -> {
                    // Log successful creation and return the response
                    log.info("User created successfully with username: {}", userResponse.getUsername());
                    return ExceptionUtil.createBuildResponse(userResponse, HttpStatus.CREATED);
                });

    }

    /**
     * Update an existing user by ID
     *
     * @param id      The ID of the user to update
     * @param request The new data for the user
     * @return A CompletableFuture containing the updated user
     */
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<?>> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest request) {
        log.info("Received request to update user with ID: {}", id);
        return userService.update(id, request)
                .thenApply(response -> ExceptionUtil.createBuildResponse(response, HttpStatus.OK));

    }

    /**
     * Get user by ID
     *
     * @param id The ID of the user to fetch
     * @return A CompletableFuture containing the user details
     */
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<?>> getUserById(@PathVariable Long id) {
        log.info("Received request to fetch user with ID: {}", id);

        return userService.getById(id)
                .thenApply(response -> ExceptionUtil.createBuildResponse(response, HttpStatus.OK));

    }

    /**
     * Get all users
     *
     * @return A CompletableFuture containing a set of all user details
     */
    @GetMapping
    public CompletableFuture<ResponseEntity<?>> getAllUsers() {
        log.info("Received request to fetch all users");

        return userService.getAll()
                .thenApply(users -> ExceptionUtil.createBuildResponse(users, HttpStatus.OK));

    }
}
