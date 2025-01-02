package com.vijay.User_Master.controller;

import com.vijay.User_Master.Helper.ExceptionUtil;
import com.vijay.User_Master.config.security.CustomUserDetailsService;
import com.vijay.User_Master.config.security.JwtTokenProvider;
import com.vijay.User_Master.dto.JwtResponse;
import com.vijay.User_Master.dto.RefreshTokenDto;
import com.vijay.User_Master.dto.RefreshTokenRequest;
import com.vijay.User_Master.dto.UserResponse;
import com.vijay.User_Master.service.RefreshTokenService;
import com.vijay.User_Master.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tokens")
@AllArgsConstructor
@Log4j2
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private JwtTokenProvider jwtTokenProvider;
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/regenerate-token")
    public ResponseEntity<?> regenerateToken(@RequestBody RefreshTokenRequest request) {
        RefreshTokenDto refreshToken = refreshTokenService.findByToken(request.getRefreshToken());
        RefreshTokenDto verifiedRefreshToken = refreshTokenService.verifyRefreshToken(refreshToken);
        UserResponse user = refreshTokenService.getUser(verifiedRefreshToken);

        log.info("Refresh Token: {}", refreshToken);
        log.info("Verified Refresh Token: {}", verifiedRefreshToken);
        log.info("User: {}", user);

        // Re-generate JWT token
        UserDetails userDetails = customUserDetailsService.loadUserByUsernameOrEmail(user.getUsername(), user.getEmail()); // Ensure this is not null
        log.info("User Details: {}", userDetails);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String newJwtToken = jwtTokenProvider.generateToken(authentication);
        log.info("New JWT Token: {}", newJwtToken);

        // Create new refresh token
        RefreshTokenDto newRefreshTokenDto = refreshTokenService.createRefreshToken(user.getUsername(), user.getEmail());
        log.info("New Refresh Token: {}", newRefreshTokenDto);

        JwtResponse jwtResponse = JwtResponse.builder()
                .jwtToken(newJwtToken)
                .refreshTokenDto(newRefreshTokenDto)
                .user(user).build();
        log.info("JWT Response: {}", jwtResponse);
        return ExceptionUtil.createBuildResponse(jwtResponse, HttpStatus.OK);
    }



    @PostMapping
    public ResponseEntity<?> createRefreshToken(@RequestParam String usernameOrEmail) {
        RefreshTokenDto refreshToken = refreshTokenService.createRefreshToken(usernameOrEmail,usernameOrEmail);
        return ExceptionUtil.createBuildResponse(refreshToken, HttpStatus.OK);
    }

    @GetMapping("/{token}")
    public ResponseEntity<?> findByToken(@PathVariable String token) {
        RefreshTokenDto refreshToken = refreshTokenService.findByToken(token);
        return ExceptionUtil.createBuildResponse(refreshToken, HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyRefreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        RefreshTokenDto verifiedToken = refreshTokenService.verifyRefreshToken(refreshTokenDto);
        return ExceptionUtil.createBuildResponse(verifiedToken, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<?> getUser(@RequestBody RefreshTokenDto refreshTokenDto) {
        UserResponse user = refreshTokenService.getUser(refreshTokenDto);
        return ExceptionUtil.createBuildResponse(user, HttpStatus.OK);
    }
}
