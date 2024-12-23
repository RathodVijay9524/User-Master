package com.vijay.User_Master.service.impl;

import com.vijay.User_Master.config.security.JwtTokenProvider;
import com.vijay.User_Master.config.security.model.LoginJWTResponse;
import com.vijay.User_Master.config.security.model.LoginRequest;
import com.vijay.User_Master.dto.UserRequest;
import com.vijay.User_Master.dto.UserResponse;
import com.vijay.User_Master.entity.Role;
import com.vijay.User_Master.entity.User;
import com.vijay.User_Master.exceptions.ResourceNotFoundException;
import com.vijay.User_Master.exceptions.UserAlreadyExistsException;
import com.vijay.User_Master.repository.RoleRepository;
import com.vijay.User_Master.repository.UserRepository;
import com.vijay.User_Master.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;

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


            // Assign the default role (USER)
            Role role = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> {
                        log.error("Role 'USER' not found");
                        return new RuntimeException("Role Not found with Name");
                    });
            user.setRoles(Set.of(role));

            user.setActive(true);
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
        return CompletableFuture.supplyAsync(() -> {
            User user = userRepository.findById(aLong)
                    .orElseThrow(() -> {
                        log.error("User with ID '{}' not found", aLong);
                        return new ResourceNotFoundException("USER", "ID", aLong);
                    });
            return mapper.map(user, UserResponse.class);
        });
    }


    @Override
    public CompletableFuture<Set<UserResponse>> getAll() {
        return CompletableFuture.supplyAsync(() ->
                userRepository.findAll().stream()
                        .map(user -> mapper.map(user, UserResponse.class))
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public CompletableFuture<UserResponse> update(Long id, UserRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("USER", "ID", id));

            // Update user fields only if they are provided in the request
            if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
                if (userRepository.existsByUsername(request.getUsername())) {
                    throw new UserAlreadyExistsException("Username is already taken");
                }
                user.setUsername(request.getUsername());
            }

            if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
                if (userRepository.existsByEmail(request.getEmail())) {
                    throw new UserAlreadyExistsException("Email is already in use");
                }
                user.setEmail(request.getEmail());
            }

            if (request.getRoles() != null && !request.getRoles().isEmpty()) {
                Set<Role> roles = request.getRoles().stream()
                        .map(roleName -> roleRepository.findByName(String.valueOf(roleName))
                                .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName)))
                        .collect(Collectors.toSet());
                user.setRoles(roles);
            }

            if (request.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            // Save the updated user in the repository
            userRepository.save(user);
            return mapper.map(user, UserResponse.class);
        });
    }

    @Override
    public CompletableFuture<Boolean> delete(Long aLong) {
        return null;
    }

    @Override
    public LoginJWTResponse login(LoginRequest req) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(req.getUsername());

        String token = jwtTokenProvider.generateToken(authentication);

        UserResponse response = mapper.map(userDetails, UserResponse.class);


        LoginJWTResponse jwtResponse = LoginJWTResponse.builder()
                .jwtToken(token)
                .user(response).build();

        return jwtResponse;
    }
}
