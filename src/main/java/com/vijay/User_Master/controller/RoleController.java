package com.vijay.User_Master.controller;

import com.vijay.User_Master.Helper.ExceptionUtil;
import com.vijay.User_Master.dto.RoleRequest;
import com.vijay.User_Master.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
@Log4j2
public class RoleController {

    private final RoleService roleService;

    /**
     * Create a new role.
     * This endpoint creates a new role based on the provided request and returns the created role.
     *
     * @param roleRequest The request containing role details.
     * @return A CompletableFuture containing the response with the created role.
     */
    /**
     * Create a new role.
     * This endpoint creates a new role based on the provided request and returns the created role.
     *
     * @param roleRequest The request containing role details.
     * @return A CompletableFuture containing the response with the created role.
     */
    @PostMapping
    public CompletableFuture<ResponseEntity<?>> createRole(@RequestBody RoleRequest roleRequest) {
        log.info("Received request to create role with name: {}", roleRequest.getName());

        // Call the service to create the role asynchronously
        return roleService.create(roleRequest)
                .thenApply(roleResponse -> {
                    // Log successful role creation and return the response
                    log.info("Role created successfully with name: {}", roleResponse.getName());
                    return ExceptionUtil.createBuildResponse(roleResponse, HttpStatus.CREATED); // Ensure correct response type
                });
    }

    /**
     * Get Role by ID.
     * This endpoint fetches a role by its ID.
     *
     * @param id The ID of the role to fetch.
     * @return A CompletableFuture containing the role details.
     */
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<?>> getRoleById(@PathVariable Long id) {
        log.info("Received request to fetch role with ID: {}", id);

        // Call the service to fetch the role asynchronously
        return roleService.getById(id)
                .thenApply(roleResponse -> {
                    log.info("Role with ID '{}' fetched successfully", id);
                    return ExceptionUtil.createBuildResponse(roleResponse, HttpStatus.OK); // Ensure correct response type
                });
    }

    /**
     * Get all roles.
     * This endpoint fetches all roles from the system.
     *
     * @return A CompletableFuture containing all roles.
     */
    @GetMapping
    public CompletableFuture<ResponseEntity<?>> getAllRoles() {
        log.info("Received request to fetch all roles.");

        // Call the service to fetch all roles asynchronously
        return roleService.getAll()
                .thenApply(roles -> {
                    log.info("Fetched {} roles successfully", roles.size());
                    return ExceptionUtil.createBuildResponse(roles, HttpStatus.OK); // Ensure correct response type
                });
    }

    /**
     * Update Role.
     * This endpoint updates an existing role based on the provided ID and request data.
     *
     * @param id      The ID of the role to update.
     * @param request The request containing the updated role data.
     * @return A CompletableFuture containing the updated role.
     */
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<?>> updateRole(@PathVariable Long id, @RequestBody RoleRequest request) {
        log.info("Received request to update role with ID: {}", id);

        // Call the service to update the role asynchronously
        return roleService.update(id, request)
                .thenApply(roleResponse -> {
                    log.info("Role with ID '{}' updated successfully", id);
                    return ExceptionUtil.createBuildResponse(roleResponse, HttpStatus.OK); // Ensure correct response type
                });
    }

    /**
     * Delete Role.
     * This endpoint deletes a role based on the provided ID.
     *
     * @param id The ID of the role to delete.
     * @return A CompletableFuture containing a boolean indicating success.
     */
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<?>> deleteRole(@PathVariable Long id) {
        log.info("Received request to delete role with ID: {}", id);

        // Call the service to delete the role asynchronously
        return roleService.delete(id)
                .thenApply(success -> {
                    log.info("Role with ID '{}' deleted successfully", id);
                    return ExceptionUtil.createBuildResponse(success, HttpStatus.OK); // Ensure correct response type
                });
    }

}
