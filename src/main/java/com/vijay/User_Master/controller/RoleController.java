package com.vijay.User_Master.controller;

import com.vijay.User_Master.Helper.ExceptionUtil;
import com.vijay.User_Master.dto.RoleRequest;
import com.vijay.User_Master.dto.RoleUpdateRequest;
import com.vijay.User_Master.dto.UserRoleRequest;
import com.vijay.User_Master.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // ============= NEW ROLE MANAGEMENT ENDPOINTS =============

    /**
     * Get all active roles.
     * This endpoint fetches all active roles from the system.
     * Only accessible by ADMIN users.
     *
     * @return ResponseEntity containing all active roles.
     */
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllActiveRoles() {
        log.info("Received request to fetch all active roles");
        
        try {
            var activeRoles = roleService.getAllActiveRoles();
            log.info("Fetched {} active roles successfully", activeRoles.size());
            return ExceptionUtil.createBuildResponse(activeRoles, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching active roles: {}", e.getMessage());
            return ExceptionUtil.createBuildResponse("Error fetching active roles", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update role details.
     * This endpoint updates role name and/or status.
     * Only accessible by ADMIN users.
     *
     * @param roleId The ID of the role to update.
     * @param updateRequest The request containing updated role details.
     * @return ResponseEntity containing the updated role.
     */
    @PatchMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateRoleDetails(@PathVariable Long roleId, @RequestBody RoleUpdateRequest updateRequest) {
        log.info("Received request to update role details for role ID: {}", roleId);
        
        try {
            var updatedRole = roleService.updateRole(roleId, updateRequest);
            log.info("Role with ID '{}' details updated successfully", roleId);
            return ExceptionUtil.createBuildResponse(updatedRole, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating role details: {}", e.getMessage());
            return ExceptionUtil.createBuildResponse("Error updating role details: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Activate a role.
     * This endpoint activates a role by setting isActive to true.
     * Only accessible by ADMIN users.
     *
     * @param roleId The ID of the role to activate.
     * @return ResponseEntity indicating success.
     */
    @PatchMapping("/{roleId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateRole(@PathVariable Long roleId) {
        log.info("Received request to activate role with ID: {}", roleId);
        
        try {
            roleService.activateRole(roleId);
            log.info("Role with ID '{}' activated successfully", roleId);
            return ExceptionUtil.createBuildResponse("Role activated successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error activating role: {}", e.getMessage());
            return ExceptionUtil.createBuildResponse("Error activating role: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deactivate a role.
     * This endpoint deactivates a role by setting isActive to false.
     * Only accessible by ADMIN users.
     *
     * @param roleId The ID of the role to deactivate.
     * @return ResponseEntity indicating success.
     */
    @PatchMapping("/{roleId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateRole(@PathVariable Long roleId) {
        log.info("Received request to deactivate role with ID: {}", roleId);
        
        try {
            roleService.deactivateRole(roleId);
            log.info("Role with ID '{}' deactivated successfully", roleId);
            return ExceptionUtil.createBuildResponse("Role deactivated successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error deactivating role: {}", e.getMessage());
            return ExceptionUtil.createBuildResponse("Error deactivating role: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ============= USER ROLE ASSIGNMENT ENDPOINTS =============

    /**
     * Assign roles to a user.
     * This endpoint assigns new roles to a user while keeping existing roles.
     * Only accessible by ADMIN users.
     *
     * @param userRoleRequest The request containing user ID and role IDs to assign.
     * @return ResponseEntity containing the updated user.
     */
    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignRolesToUser(@RequestBody UserRoleRequest userRoleRequest) {
        log.info("Received request to assign roles to user ID: {}", userRoleRequest.getUserId());
        
        try {
            var updatedUser = roleService.assignRolesToUser(userRoleRequest);
            log.info("Roles assigned successfully to user ID: {}", userRoleRequest.getUserId());
            return ExceptionUtil.createBuildResponse(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error assigning roles to user: {}", e.getMessage());
            return ExceptionUtil.createBuildResponse("Error assigning roles: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Remove roles from a user.
     * This endpoint removes specified roles from a user.
     * Only accessible by ADMIN users.
     *
     * @param userRoleRequest The request containing user ID and role IDs to remove.
     * @return ResponseEntity containing the updated user.
     */
    @PostMapping("/remove")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeRolesFromUser(@RequestBody UserRoleRequest userRoleRequest) {
        log.info("Received request to remove roles from user ID: {}", userRoleRequest.getUserId());
        
        try {
            var updatedUser = roleService.removeRolesFromUser(userRoleRequest);
            log.info("Roles removed successfully from user ID: {}", userRoleRequest.getUserId());
            return ExceptionUtil.createBuildResponse(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error removing roles from user: {}", e.getMessage());
            return ExceptionUtil.createBuildResponse("Error removing roles: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Replace user roles.
     * This endpoint replaces all existing roles of a user with new ones.
     * Only accessible by ADMIN users.
     *
     * @param userRoleRequest The request containing user ID and new role IDs.
     * @return ResponseEntity containing the updated user.
     */
    @PutMapping("/replace")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> replaceUserRoles(@RequestBody UserRoleRequest userRoleRequest) {
        log.info("Received request to replace roles for user ID: {}", userRoleRequest.getUserId());
        
        try {
            var updatedUser = roleService.replaceUserRoles(userRoleRequest);
            log.info("Roles replaced successfully for user ID: {}", userRoleRequest.getUserId());
            return ExceptionUtil.createBuildResponse(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error replacing user roles: {}", e.getMessage());
            return ExceptionUtil.createBuildResponse("Error replacing roles: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get user roles.
     * This endpoint fetches all roles assigned to a specific user.
     * Only accessible by ADMIN users.
     *
     * @param userId The ID of the user whose roles to fetch.
     * @return ResponseEntity containing the user's roles.
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserRoles(@PathVariable Long userId) {
        log.info("Received request to fetch roles for user ID: {}", userId);
        
        try {
            var userRoles = roleService.getUserRoles(userId);
            log.info("Fetched {} roles for user ID: {}", userRoles.size(), userId);
            return ExceptionUtil.createBuildResponse(userRoles, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching user roles: {}", e.getMessage());
            return ExceptionUtil.createBuildResponse("Error fetching user roles: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Check if role exists.
     * This endpoint checks if a role exists by ID.
     * Only accessible by ADMIN users.
     *
     * @param roleId The ID of the role to check.
     * @return ResponseEntity indicating if the role exists.
     */
    @GetMapping("/{roleId}/exists")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> checkRoleExists(@PathVariable Long roleId) {
        log.info("Received request to check if role exists with ID: {}", roleId);
        
        try {
            boolean exists = roleService.roleExists(roleId);
            log.info("Role existence check for ID '{}': {}", roleId, exists);
            return ExceptionUtil.createBuildResponse(exists, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error checking role existence: {}", e.getMessage());
            return ExceptionUtil.createBuildResponse("Error checking role existence: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
