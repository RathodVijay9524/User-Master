package com.vijay.User_Master.service.impl;

import com.vijay.User_Master.dto.RoleRequest;
import com.vijay.User_Master.dto.RoleResponse;
import com.vijay.User_Master.repository.RoleRepository;
import com.vijay.User_Master.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    @Override
    public CompletableFuture<Boolean> create(RoleRequest request) {
        return null;
    }

    @Override
    public CompletableFuture<RoleResponse> getById(Long aLong) {
        return null;
    }

    @Override
    public CompletableFuture<Set<RoleResponse>> getAll() {
        return null;
    }

    @Override
    public CompletableFuture<RoleResponse> update(Long aLong, RoleRequest request) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> delete(Long aLong) {
        return null;
    }
}
