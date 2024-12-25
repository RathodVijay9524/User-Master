package com.vijay.User_Master.service;

import com.vijay.User_Master.config.security.model.LoginJWTResponse;
import com.vijay.User_Master.config.security.model.LoginRequest;
import com.vijay.User_Master.dto.UserRequest;
import com.vijay.User_Master.dto.UserResponse;

import com.vijay.User_Master.dto.form.ChangePasswordForm;

import com.vijay.User_Master.dto.form.ForgotPasswordForm;
import com.vijay.User_Master.dto.form.UnlockForm;

import java.util.concurrent.CompletableFuture;

public interface AuthService {

    boolean unlockAccount(UnlockForm form);

    // For reset password
    boolean forgotPassword(ForgotPasswordForm from, String email);


    boolean changePassword(ChangePasswordForm form);

    boolean existsByUsernameOrEmail(String usernameOrEmail);
    LoginJWTResponse login(LoginRequest req);
    CompletableFuture<Object> registerForAdminUser(UserRequest request,String url);
    UserResponse registerForNormalUser(UserRequest request);

}
