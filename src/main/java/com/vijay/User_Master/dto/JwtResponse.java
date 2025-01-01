package com.vijay.User_Master.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private String jwtToken;
    private UserResponse user;
    private RefreshTokenDto refreshTokenDto; // Ensure this is of type RefreshTokenDto
}

