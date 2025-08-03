package com.vijay.User_Master.repository;

import com.vijay.User_Master.entity.RefreshToken;
import com.vijay.User_Master.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);


    Optional<RefreshToken> findByUsername(String identifier);
}