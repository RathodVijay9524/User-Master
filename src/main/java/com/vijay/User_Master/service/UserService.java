package com.vijay.User_Master.service;


import com.vijay.User_Master.config.security.model.LoginJWTResponse;
import com.vijay.User_Master.config.security.model.LoginRequest;
import com.vijay.User_Master.dto.UserRequest;
import com.vijay.User_Master.dto.UserResponse;

public interface UserService extends iCrudService<UserRequest, UserResponse,Long> {

    LoginJWTResponse login(LoginRequest req);

}
