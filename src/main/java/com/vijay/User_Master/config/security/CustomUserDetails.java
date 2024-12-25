package com.vijay.User_Master.config.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vijay.User_Master.entity.AccountStatus;
import com.vijay.User_Master.entity.Role;
import com.vijay.User_Master.entity.User;
import com.vijay.User_Master.entity.Worker;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String name;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private Set<Role> roles;
    private String phoNo;
    private boolean isDeleted;
    @JsonIgnore
    private List<Worker> workers;
    private AccountStatus accountStatus;

    public CustomUserDetails(Long id, String name, String username, String email, String password, Set<Role> roles, String phoNo, boolean isDeleted, List<Worker> workers, AccountStatus accountStatus) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.phoNo = phoNo;
        this.isDeleted = isDeleted;
        this.workers = workers;
        this.accountStatus = accountStatus;
    }

    public static CustomUserDetails build(User user) {
        return new CustomUserDetails(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles(),
                user.getPhoNo(),
                user.isDeleted(),
                user.getWorkers(),
                user.getAccountStatus()
        );
    }

    public static CustomUserDetails build(Worker worker) {
        return new CustomUserDetails(
                worker.getId(),
                worker.getName(),
                worker.getUsername(),
                worker.getEmail(),
                worker.getPassword(),
                worker.getRoles(),
                worker.getPhoNo(),
                worker.isDeleted(),
                null, // Workers don't have associated workers, so set it to null
                worker.getAccountStatus()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}

