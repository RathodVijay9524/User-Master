package com.vijay.User_Master.controller;

import com.vijay.User_Master.Helper.CommonUtils;
import com.vijay.User_Master.Helper.ExceptionUtil;
import com.vijay.User_Master.config.security.CustomUserDetails;
import com.vijay.User_Master.config.security.model.LoginJWTResponse;
import com.vijay.User_Master.config.security.model.LoginRequest;
import com.vijay.User_Master.dto.UserRequest;
import com.vijay.User_Master.dto.UserResponse;
import com.vijay.User_Master.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    /**
     * Login endpoint for user authentication.
     *
     * @param request The login request containing username and password.
     * @return A ResponseEntity containing the JWT response or an error message.
     */
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LoginJWTResponse login = authService.login(request);
        return ExceptionUtil.createBuildResponse(login, HttpStatus.OK);
    }

    /**
     * Endpoint for registering an admin user.
     *
     * @param request The user request data.
     * @return A ResponseEntity containing the UserResponse or an error message.
     */
    @PostMapping("/register/admin")
    public CompletableFuture<ResponseEntity<?>> registerAdmin(@RequestBody UserRequest request) {
        return authService.registerForAdminUser(request)
                .thenApply(response -> {
                    return ExceptionUtil.createBuildResponse(response, HttpStatus.OK);
                });

    }

    /**
     * Endpoint for registering a normal user (worker).
     *
     * @param request The user request data.
     * @return A ResponseEntity containing the UserResponse or an error message.
     */
    @PostMapping(value = "/register/worker", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registerWorker(@RequestBody UserRequest request) {
        UserResponse response = authService.registerForNormalUser(request);
        return ExceptionUtil.createBuildResponse(response, HttpStatus.OK);


    }

    /**
     * Endpoint to get the currently logged-in user's details.
     *
     * @return A ResponseEntity containing the CustomUserDetails or an error message.
     */
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser() {
        try {
            CustomUserDetails loggedInUser= CommonUtils.getLoggedInUser();
            return ResponseEntity.ok(loggedInUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated....!!");
        }
    }

}


