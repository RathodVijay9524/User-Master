package com.vijay.User_Master.service;

import com.vijay.User_Master.dto.RefreshTokenDto;
import com.vijay.User_Master.dto.UserResponse;
import com.vijay.User_Master.dto.WorkerResponse;

public interface RefreshTokenService {

    //create
    RefreshTokenDto createRefreshToken(String username,String email);

    // find by token
    RefreshTokenDto findByToken(String token);
//verify

    RefreshTokenDto verifyRefreshToken(RefreshTokenDto refreshTokenDto);

    UserResponse getUser(RefreshTokenDto dto);

    WorkerResponse getWorker(RefreshTokenDto verifiedRefreshToken);
}
