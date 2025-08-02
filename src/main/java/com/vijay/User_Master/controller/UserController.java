package com.vijay.User_Master.controller;

import com.vijay.User_Master.Helper.ExceptionUtil;
import com.vijay.User_Master.dto.PageableResponse;
import com.vijay.User_Master.dto.UserRequest;
import com.vijay.User_Master.dto.UserResponse;
import com.vijay.User_Master.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    /*
    *      All users /api/users/filter
           Active users	/api/users/filter?isDeleted=false&isActive=true
           Deleted users	/api/users/filter?isDeleted=true
           Expired users	/api/users/filter?isDeleted=false&isActive=false
    *
    * */
    @GetMapping("/filter")
    public ResponseEntity<?> getUsersWithFilter(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String keyword
    ) {
        log.info("Fetching users with filters - isDeleted: {}, isActive: {}, page: {}, size: {}", isDeleted, isActive, pageNumber, pageSize);

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<UserResponse> userPage = userService.getUsersWithFilter(isDeleted, isActive,keyword, pageable);

        return ExceptionUtil.createBuildResponse(userPage, HttpStatus.OK);
    }

    /**
     * Get all users
     *  api/users?isDeleted=false&isActive=true
     *  api/users?isDeleted=false&isActive=false
     *  api/users?isActive=false
     *  api/users?isDeleted=false
     * @return A CompletableFuture containing a set of all user details
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(required = false) Boolean isActive
    ) {
        log.info("Received request to fetch users: isDeleted={}, isActive={}", isDeleted, isActive);
        PageableResponse<UserResponse> response = userService.getUsersWithFilters(
                pageNumber, pageSize, sortBy, sortDir, isDeleted, isActive);
        return ExceptionUtil.createBuildResponse(response, HttpStatus.OK);
    }

    /*
    *  api/users/active - only active user finds
    * */

    @GetMapping("/active")
    public ResponseEntity<PageableResponse<UserResponse>> getAllActiveUsers(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        PageableResponse<UserResponse> users = userService.getAllActiveUsers(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/deleted")
    public ResponseEntity<PageableResponse<UserResponse>> getAllDeletedUsers(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        PageableResponse<UserResponse> users = userService.getAllDeletedUsers(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(users, HttpStatus.OK);
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
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        log.info("Received request to fetch user with ID: {}", id);
        UserResponse response = userService.getByIdForUser(id);
        return ExceptionUtil.createBuildResponse(response, HttpStatus.OK);
    }


    // ✅ NEW: Update account status (e.g., activate/deactivate)
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateAccountStatus(
            @PathVariable Long id,
            @RequestParam Boolean isActive) {
        log.info("Received request to update account status for user ID: {} to active={}", id, isActive);
        userService.updateAccountStatus(id, isActive);
        return ExceptionUtil.createBuildResponse("Account status updated successfully", HttpStatus.OK);
    }

    // ✅ NEW: Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteUser(@PathVariable Long id) {
        log.info("Soft deleting user ID: {}", id);
        userService.softDeleteUser(id);
        return ExceptionUtil.createBuildResponse("User soft-deleted", HttpStatus.OK);
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<?> restoreUser(@PathVariable Long id) {
        log.info("Restoring soft-deleted user ID: {}", id);
        userService.restoreUser(id);
        return ExceptionUtil.createBuildResponse("User restored", HttpStatus.OK);
    }

    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<?> permanentlyDeleteUser(@PathVariable Long id) {
        log.info("Permanently deleting user ID: {}", id);
        userService.permanentlyDelete(id);
        return ExceptionUtil.createBuildResponse("User permanently deleted", HttpStatus.NO_CONTENT);
    }
}
