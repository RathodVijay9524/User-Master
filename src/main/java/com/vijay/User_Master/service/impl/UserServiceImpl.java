package com.vijay.User_Master.service.impl;

import com.vijay.User_Master.dto.UserRespons;
import com.vijay.User_Master.repository.RoleRepository;
import com.vijay.User_Master.repository.UserRepository;
import com.vijay.User_Master.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public CompletableFuture<Boolean> create(UserService request) {
        return null;
    }

    @Override
    public CompletableFuture<UserRespons> getById(Long aLong) {
        return null;
    }

    @Override
    public CompletableFuture<Set<UserRespons>> getAll() {
        return null;
    }

    @Override
    public CompletableFuture<UserRespons> update(Long aLong, UserService request) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> delete(Long aLong) {
        return null;
    }
}
