package com.vijay.User_Master.controller;

import com.vijay.User_Master.dto.RoleRequest;
import com.vijay.User_Master.dto.RoleResponse;
import com.vijay.User_Master.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
@Log4j2
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public CompletableFuture<ResponseEntity<RoleResponse>> createRole(@RequestBody RoleRequest roleRequest) {
        log.info("Received request to create role with name: {}", roleRequest.getName());
        // Call the service to create the role asynchronously
        return roleService.create(roleRequest)
                .thenApply(roleResponse -> {
                    // Log successful role creation and return the response
                    log.info("Role created successfully with name: {}", roleResponse.getName());
                    return new ResponseEntity<>(roleResponse, HttpStatus.CREATED);
                })
                .exceptionally(ex -> {
                    // Log error if any exception occurs during role creation
                    log.error("Error during role creation: {}", ex.getMessage());
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

}
