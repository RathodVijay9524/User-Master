package com.vijay.User_Master.dto;

import lombok.*;

import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefreshTokenDto {
    private int id;
    private String token;
    private Instant expiryDate;
    private String username; // Added Username as Identifier
    private String email;


    // Method to check if token is expired
    /*public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }*/
}