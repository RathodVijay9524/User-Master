package com.vijay.User_Master.controller;

import com.vijay.User_Master.dto.RoleRequest;
import com.vijay.User_Master.dto.RoleResponse;
import com.vijay.User_Master.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
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

    // Get Role by ID
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<RoleResponse>> getRoleById(@PathVariable Long id) {
        log.info("Received request to fetch role with ID: {}", id);

        return roleService.getById(id)
                .thenApply(roleResponse -> {
                    log.info("Role with ID '{}' fetched successfully", id);
                    return ResponseEntity.ok(roleResponse);
                })
                .exceptionally(ex -> {
                    log.error("Error fetching role by ID: {}", ex.getMessage());
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                });
    }

    // Get all roles
    @GetMapping
    public CompletableFuture<ResponseEntity<Set<RoleResponse>>> getAllRoles() {
        log.info("Received request to fetch all roles.");

        return roleService.getAll()
                .thenApply(roles -> {
                    log.info("Fetched {} roles successfully", roles.size());
                    return ResponseEntity.ok(roles);
                })
                .exceptionally(ex -> {
                    log.error("Error fetching all roles: {}", ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                });
    }

    // Update Role
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<RoleResponse>> updateRole(@PathVariable Long id, @RequestBody RoleRequest request) {
        log.info("Received request to update role with ID: {}", id);

        return roleService.update(id, request)
                .thenApply(roleResponse -> {
                    log.info("Role with ID '{}' updated successfully", id);
                    return ResponseEntity.ok(roleResponse);
                })
                .exceptionally(ex -> {
                    log.error("Error updating role: {}", ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                });
    }

    // Delete Role
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Boolean>> deleteRole(@PathVariable Long id) {
        log.info("Received request to delete role with ID: {}", id);

        return roleService.delete(id)
                .thenApply(success -> {
                    log.info("Role with ID '{}' deleted successfully", id);
                    return ResponseEntity.ok(success);
                })
                .exceptionally(ex -> {
                    log.error("Error deleting role: {}", ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
                });
    }


}
