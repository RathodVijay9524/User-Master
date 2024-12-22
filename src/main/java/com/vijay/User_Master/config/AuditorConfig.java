package com.vijay.User_Master.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorConfig implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {

        return Optional.of(1);
    }
}
