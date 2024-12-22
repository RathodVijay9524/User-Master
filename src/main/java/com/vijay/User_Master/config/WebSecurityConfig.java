package com.vijay.User_Master.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
         * This configuration bypasses all security controls by directly building the
         * HttpSecurity object without defining any authentication or authorization rules.
         *
         * Effectively, this allows all incoming requests to pass through without any
         * security checks. This should only be used for development or specific scenarios
         * where security is intentionally disabled.
         */
        return http.build();
    }
}
