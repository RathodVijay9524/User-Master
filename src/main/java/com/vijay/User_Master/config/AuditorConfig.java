package com.vijay.User_Master.config;

import com.vijay.User_Master.Helper.CommonUtils;
import com.vijay.User_Master.config.security.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorConfig implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        CustomUserDetails loggedInUser = CommonUtils.getLoggedInUser();
        return Optional.of(Math.toIntExact(loggedInUser.getId()));
    }
}
