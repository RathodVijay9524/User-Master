package com.vijay.User_Master.dto;

import com.vijay.User_Master.entity.Role;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private String name;

    private String username;

    private String email;

    private String password;
    private Set<String> roles; // Roles are strings like "ROLE_ADMIN"

}
