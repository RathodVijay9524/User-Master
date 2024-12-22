package com.vijay.User_Master.dto;

import com.vijay.User_Master.entity.Role;
import lombok.*;

import java.util.Set;

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
    private Set<Role> roles;
    private boolean isActive;
    private boolean isDeleted;


}
