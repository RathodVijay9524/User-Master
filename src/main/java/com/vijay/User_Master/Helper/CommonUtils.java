package com.vijay.User_Master.Helper;

import com.vijay.User_Master.config.security.CustomUserDetails;
import com.vijay.User_Master.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;

public class CommonUtils {

    @Transactional
    public static CustomUserDetails getLoggedInUser() {
        try {
            CustomUserDetails logUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return logUser;
        } catch (Exception e) {
            throw new RuntimeException("User is not authenticated.", e);
        }
    }
}
