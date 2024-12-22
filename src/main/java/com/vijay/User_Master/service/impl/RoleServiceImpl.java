package com.vijay.User_Master.service.impl;

import com.vijay.User_Master.dto.RoleRequest;
import com.vijay.User_Master.dto.RoleResponse;
import com.vijay.User_Master.entity.Role;
import com.vijay.User_Master.repository.RoleRepository;
import com.vijay.User_Master.service.RoleService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper mapper;
    @Override
    public CompletableFuture<RoleResponse> create(RoleRequest request) {
        return CompletableFuture.supplyAsync(()->{
            Role role = mapper.map(request, Role.class);
            role.setActive(true);
            roleRepository.save(role);
            return mapper.map(role,RoleResponse.class);
        });
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
