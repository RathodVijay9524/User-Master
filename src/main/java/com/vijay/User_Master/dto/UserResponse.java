package com.vijay.User_Master.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private boolean isActive;
    private boolean isDeleted;


}
