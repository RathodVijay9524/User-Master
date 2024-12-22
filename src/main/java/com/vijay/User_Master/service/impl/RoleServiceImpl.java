package com.vijay.User_Master.service.impl;

import com.vijay.User_Master.dto.RoleRequest;
import com.vijay.User_Master.dto.RoleResponse;
import com.vijay.User_Master.entity.Role;
import com.vijay.User_Master.exceptions.ResourceNotFoundException;
import com.vijay.User_Master.repository.RoleRepository;
import com.vijay.User_Master.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper mapper;

    // Create Role For Users
    @Override
    public CompletableFuture<RoleResponse> create(RoleRequest request) {
        return CompletableFuture.supplyAsync(()->{
            Role role = mapper.map(request, Role.class);
            role.setActive(true);
            roleRepository.save(role);
            return mapper.map(role,RoleResponse.class);
        });
    }

    // Get Role by ID
    @Override
    public CompletableFuture<RoleResponse> getById(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            // Log the fetch attempt
            log.info("Attempting to fetch role with ID: {}", id);

            // Fetch the role by ID and handle possible not found exception
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Role with ID '{}' not found", id);
                        return new ResourceNotFoundException("Role", "id", id);
                    });

            // Log success
            log.info("Role with ID '{}' found", id);

            // Map and return the Role as RoleResponse
            return mapper.map(role, RoleResponse.class);
        });
    }

    // Get all roles
    @Override
    public CompletableFuture<Set<RoleResponse>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            // Log the attempt to fetch all roles
            log.info("Attempting to fetch all roles.");

            // Fetch all roles and map them to RoleResponse
            Set<Role> roles = new HashSet<>(roleRepository.findAll());
            Set<RoleResponse> roleResponses = roles.stream()
                    .map(role -> mapper.map(role, RoleResponse.class))
                    .collect(Collectors.toSet());

            // Log success
            log.info("Fetched {} roles successfully.", roleResponses.size());

            return roleResponses;
        });
    }

    // Update an existing role
    @Override
    public CompletableFuture<RoleResponse> update(Long id, RoleRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            // Log the update attempt
            log.info("Attempting to update role with ID: {}", id);

            // Fetch the existing role by ID
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Role with ID '{}' not found", id);
                        return new ResourceNotFoundException("Role", "id", id);
                    });

            // Update fields only if provided in the request
            if (request.getName() != null) {
                role.setName(request.getName());
            }
            // Save the updated role
            roleRepository.save(role);

            // Log success
            log.info("Role with ID '{}' updated successfully", id);

            // Map and return the updated Role as RoleResponse
            return mapper.map(role, RoleResponse.class);
        });
    }

    // Delete a role by ID
    @Override
    public CompletableFuture<Boolean> delete(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            // Log the delete attempt
            log.info("Attempting to delete role with ID: {}", id);

            // Fetch the role to be deleted
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Role with ID '{}' not found", id);
                        return new ResourceNotFoundException("Role", "id", id);
                    });

            // Delete the role
            roleRepository.delete(role);

            // Log success
            log.info("Role with ID '{}' deleted successfully", id);

            return true;
        });
    }

}
