package com.vijay.User_Master.config.security.model;

import com.vijay.User_Master.dto.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginJWTResponse {

    private String jwtToken;
    private UserResponse user;

}
