package com.vijay.User_Master.service.impl;

import com.vijay.User_Master.dto.RefreshTokenDto;
import com.vijay.User_Master.dto.UserResponse;
import com.vijay.User_Master.dto.WorkerResponse;
import com.vijay.User_Master.entity.RefreshToken;
import com.vijay.User_Master.entity.User;
import com.vijay.User_Master.entity.Worker;
import com.vijay.User_Master.exceptions.ResourceNotFoundException;
import com.vijay.User_Master.repository.RefreshTokenRepository;
import com.vijay.User_Master.repository.UserRepository;
import com.vijay.User_Master.repository.WorkerRepository;
import com.vijay.User_Master.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Log4j2
@Service
@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;
    private WorkerRepository workerRepository;
    private ModelMapper mapper;

    @Override
    public RefreshTokenDto createRefreshToken(String usernameOrEmail, String email) {
        log.info("Creating refresh token for: {}", usernameOrEmail);

        // Find user or worker by username or email
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, email).orElse(null);
        Worker worker = workerRepository.findByEmail(email);

        if (user == null && worker == null) {
            throw new ResourceNotFoundException("User or Worker", "identifier", usernameOrEmail);
        }

        // Create common refresh token information
        String identifier = (user != null) ? user.getUsername() : worker.getUsername();
        String emails = (user != null) ? user.getEmail() : worker.getEmail();

        // Check if a refresh token already exists for the user or worker
        RefreshToken refreshToken = refreshTokenRepository.findByUsername(identifier).orElse(null);
        if (refreshToken == null) {
            // Create new refresh token if none exists
            log.info("No existing refresh token found, creating a new one.");
            refreshToken = RefreshToken.builder()
                    .username(identifier)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusSeconds(2 * 24 * 60 * 60)) // Token expires in 2 days
                    .email(emails)
                    .build();
        } else {
            // Update the existing refresh token
            log.info("Existing refresh token found, updating the token and expiry date.");
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusSeconds(2 * 24 * 60 * 60));
        }

        refreshTokenRepository.save(refreshToken);
        return mapper.map(refreshToken, RefreshTokenDto.class);
    }


    @Override
    public RefreshTokenDto findByToken(String token) {
        log.info("Finding refresh token: {}", token);

        // Find refresh token by token string
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh-Token", "Id", token));

        log.info("Refresh token found: {}", refreshToken.getToken());

        // Convert to DTO and return
        return mapper.map(refreshToken, RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto verifyRefreshToken(RefreshTokenDto refreshTokenDto) {
        log.info("Verifying refresh token: {}", refreshTokenDto.getToken());

        // Convert DTO to entity
        var refreshToken = mapper.map(refreshTokenDto, RefreshToken.class);

        // Check if the refresh token has expired
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            log.warn("Refresh token has expired: {}", refreshToken.getToken());
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh Token Expired!!");
        }

        log.info("Refresh token is valid: {}", refreshToken.getToken());

        // Return the original DTO as it is valid
        return refreshTokenDto;
    }

    @Override
    public UserResponse getUser(RefreshTokenDto refreshTokenDto) {
        log.info("Getting user for refresh token: {}", refreshTokenDto.getToken());

        // Find refresh token by token string
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenDto.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("refresh-token", "id", refreshTokenDto.getToken()));

        // Get the user associated with the refresh token
        User user = refreshToken.getUser();
        log.info("User found: {}", user.getUsername());

        // Convert to DTO and return
        return mapper.map(user, UserResponse.class);
    }

    @Override
    public WorkerResponse getWorker(RefreshTokenDto refreshTokenDto) {
        log.info("Getting worker for refresh token: {}", refreshTokenDto.getToken());

        // Find refresh token by token string
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenDto.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("refresh-token", "id", refreshTokenDto.getToken()));

        // Get the worker associated with the refresh token
        Worker worker = refreshToken.getWorker();
        log.info("Worker found: {}", worker.getUsername());

        // Convert to DTO and return
        return mapper.map(worker, WorkerResponse.class);
    }
}

