package com.vijay.User_Master.service;

import com.vijay.User_Master.config.security.model.LoginJWTResponse;
import com.vijay.User_Master.config.security.model.LoginRequest;
import com.vijay.User_Master.dto.UserRequest;
import com.vijay.User_Master.dto.UserResponse;
import com.vijay.User_Master.dto.WorkerRequest;

import java.util.concurrent.CompletableFuture;

public interface AuthService {

    boolean existsByUsernameOrEmail(String usernameOrEmail);
    LoginJWTResponse login(LoginRequest req);


    CompletableFuture<UserResponse> registerForAdminUser(UserRequest request);
    UserResponse registerForNormalUser(UserRequest request);

}
