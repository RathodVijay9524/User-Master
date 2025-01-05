package com.vijay.User_Master.controller;

import com.vijay.User_Master.Helper.CommonUtils;
import com.vijay.User_Master.Helper.ExceptionUtil;
import com.vijay.User_Master.config.security.CustomUserDetails;
import com.vijay.User_Master.dto.ImageResponse;
import com.vijay.User_Master.dto.UserRequest;
import com.vijay.User_Master.dto.UserResponse;
import com.vijay.User_Master.entity.Role;
import com.vijay.User_Master.service.FileService;
import com.vijay.User_Master.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.ResponseUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;

    private FileService fileService;
    private ModelMapper mapper;



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

    @PostMapping("/image")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image) {
        try {
            log.info("Starting image upload process for user.");

            CustomUserDetails loggedInUser = CommonUtils.getLoggedInUser();
            log.info("Retrieved logged-in user: {}", loggedInUser);

            Set<Role> roles = loggedInUser.getRoles();

            // Filter roles to ensure only active and non-deleted roles are set
            Set<Role> activeRoles = roles.stream()
                    .filter(role -> role.isActive() && !role.isDeleted())
                    .collect(Collectors.toSet());
            log.info("Retrieved active roles for logged-in user: {}", activeRoles);

            String imageName = fileService.uploadFile(image, "images/users/");
            log.info("Uploaded file with name: {}", imageName);

            CompletableFuture<UserResponse> userFuture = userService.getById(loggedInUser.getId());
            UserResponse userResponse = userFuture.get(); // Handle ExecutionException / InterruptedException
            log.info("Fetched user details for user ID: {}", loggedInUser.getId());

            userResponse.setRoles(activeRoles); // Ensure this role exists in the database
            userResponse.setImageName(imageName);
            log.info("Set image name and roles for user.");

            UserRequest userRequest = mapper.map(userResponse, UserRequest.class);

            log.info("This image name from userRequest: {}",userRequest.getImageName());


            CompletableFuture<UserResponse> updateUserFuture = userService.update(loggedInUser.getId(), userRequest);
            UserResponse updatedUser = updateUserFuture.get(); // Ensure the update is completed
            log.info("Updated user details in the database.");

            ImageResponse imageResponse = ImageResponse.builder()
                    .imageName(imageName)
                    .success(true)
                    .message("Image uploaded successfully")
                    .status(HttpStatus.CREATED)
                    .build();

            log.info("Image upload process completed successfully.");
            return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            log.error("Role not found: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ImageResponse.builder()
                            .imageName(null)
                            .success(false)
                            .message("Image upload failed: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build());
        } catch (Exception e) {
            log.error("Unexpected error occurred during image upload: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ImageResponse.builder()
                            .imageName(null)
                            .success(false)
                            .message("Image upload failed: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build());
        }
    }



    @GetMapping(value = "/image/{userId}")
    public void serveUserImage(@PathVariable Long userId, HttpServletResponse response) throws IOException, ExecutionException, InterruptedException {
        log.info("Received request to serve image for user ID: {}", userId);

        CompletableFuture<UserResponse> userFuture = userService.getById(userId);
        UserResponse userResponse = userFuture.get(); // Handle ExecutionException / InterruptedException

        String imageName = userResponse.getImageName();
        log.info("User image name: {}", imageName);

        InputStream resource = fileService.getResource("images/users/", imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource, response.getOutputStream());
        log.info("Image served successfully for user ID: {}", userId);
    }


}
