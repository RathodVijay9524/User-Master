package com.vijay.User_Master.service.impl;

import com.vijay.User_Master.config.security.JwtTokenProvider;
import com.vijay.User_Master.config.security.model.LoginJWTResponse;
import com.vijay.User_Master.config.security.model.LoginRequest;
import com.vijay.User_Master.dto.UserRequest;
import com.vijay.User_Master.dto.UserResponse;
import com.vijay.User_Master.dto.WorkerRequest;
import com.vijay.User_Master.entity.Role;
import com.vijay.User_Master.entity.User;
import com.vijay.User_Master.entity.Worker;
import com.vijay.User_Master.exceptions.ResourceNotFoundException;
import com.vijay.User_Master.exceptions.UserAlreadyExistsException;
import com.vijay.User_Master.repository.RoleRepository;
import com.vijay.User_Master.repository.UserRepository;
import com.vijay.User_Master.repository.WorkerRepository;
import com.vijay.User_Master.service.AuthService;
import com.vijay.User_Master.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
@Log4j2
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ModelMapper mapper;
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;


    @Override
    public boolean existsByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.existsByUsername(usernameOrEmail)
                || userRepository.existsByEmail(usernameOrEmail)
                || workerRepository.existsByUsername(usernameOrEmail)
                || workerRepository.existsByEmail(usernameOrEmail);
    }

    @Override
    public LoginJWTResponse login(LoginRequest req) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(req.getUsernameOrEmail(), req.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(req.getUsernameOrEmail());

        String token = jwtTokenProvider.generateToken(authentication);

        UserResponse response = mapper.map(userDetails, UserResponse.class);

        LoginJWTResponse jwtResponse = LoginJWTResponse.builder()
                .jwtToken(token)
                .user(response).build();

        return jwtResponse;
    }


    @Override
    public CompletableFuture<UserResponse> registerForAdminUser(UserRequest request) {
        log.info("Attempting to create a new admin user with username: {}", request.getUsername());
        return CompletableFuture.supplyAsync(() -> {
            if (existsByUsernameOrEmail(request.getUsername()) || existsByUsernameOrEmail(request.getEmail())) {
                log.error("Username '{}' or email '{}' already exists", request.getUsername(), request.getEmail());
                throw new UserAlreadyExistsException("Username or email is already taken");
            }
            User user = mapper.map(request, User.class);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            Role role = roleRepository.findByName("ROLE_NORMAL").orElseThrow(() -> {
                log.error("Role 'User' not found");
                return new RuntimeException("Role not found with name 'ROLE_ADMIN'");
            });
            user.setRoles(Set.of(role));
            user.setActive(true);
            userRepository.save(user);
            log.info("Admin user with username '{}' created successfully", user.getUsername());
            return mapper.map(user, UserResponse.class);
        });
    }

    @Override
    public UserResponse registerForNormalUser(UserRequest request) {
        log.info("Attempting to create a new normal user with username: {}", request.getUsername());
        if (existsByUsernameOrEmail(request.getUsername()) || existsByUsernameOrEmail(request.getEmail())) {
            log.error("Username '{}' or email '{}' already exists", request.getUsername(), request.getEmail());
            throw new UserAlreadyExistsException("Username or email is already taken");
        }
        UserResponse currentUser = userService.getCurrentUser();
        User user = mapper.map(currentUser, User.class);

        Worker worker = mapper.map(request, Worker.class);
        worker.setPassword(passwordEncoder.encode(request.getPassword()));

        // Fetch the worker role from the repository
        Role workerRole = roleRepository.findByName("ROLE_WORKER")
                .orElseThrow(() ->  new RuntimeException("Worker role not found."));

        // Initialize the roles field if it's null
        if (worker.getRoles() == null) {
            worker.setRoles(new HashSet<>());
        }
        // Add the worker role to the roles set
        worker.getRoles().add(workerRole);
        worker.setActive(true);
        worker.setUser(user);
        workerRepository.save(worker);
        return mapper.map(worker, UserResponse.class);
    }


}


