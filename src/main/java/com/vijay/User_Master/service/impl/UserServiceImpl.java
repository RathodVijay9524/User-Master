package com.vijay.User_Master.service.impl;

import com.vijay.User_Master.dto.UserRequest;
import com.vijay.User_Master.dto.UserResponse;
import com.vijay.User_Master.entity.Role;
import com.vijay.User_Master.entity.User;
import com.vijay.User_Master.exceptions.UserAlreadyExistsException;
import com.vijay.User_Master.repository.RoleRepository;
import com.vijay.User_Master.repository.UserRepository;
import com.vijay.User_Master.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Override
    public CompletableFuture<UserResponse> create(UserRequest request) {
        // Start logging
        log.info("Attempting to create a new user with username: {}", request.getUsername());

        return CompletableFuture.supplyAsync(() -> {
            // Check if the username already exists in the database
            if (userRepository.existsByUsername(request.getUsername())) {
                log.error("Username '{}' already exists", request.getUsername());
                throw new UserAlreadyExistsException("Username is already taken");
            }

            // Check if the email already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                log.error("Email '{}' already exists", request.getEmail());
                throw new UserAlreadyExistsException("Email is already in use");
            }

            // Map the request to a User entity
            User user = mapper.map(request, User.class);
            // Encode password before saving
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setActive(true);

            // Assign the default role (USER)
            Role role = roleRepository.findByName("USER")
                    .orElseThrow(() -> {
                        log.error("Role 'USER' not found");
                        return new RuntimeException("Role Not found with Name");
                    });
            user.setRoles(Set.of(role));

            // Save the user in the repository
            userRepository.save(user);

            // Log user creation
            log.info("User with username '{}' created successfully", user.getUsername());

            // Map the saved user to UserResponse and return
            return mapper.map(user, UserResponse.class);

        });
    }

    @Override
    public CompletableFuture<UserResponse> getById(Long aLong) {
        return null;
    }

    @Override
    public CompletableFuture<Set<UserResponse>> getAll() {
        return null;
    }

    @Override
    public CompletableFuture<UserResponse> update(Long aLong, UserRequest request) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> delete(Long aLong) {
        return null;
    }

}
