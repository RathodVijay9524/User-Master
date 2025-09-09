package com.vijay.User_Master.config;

import com.vijay.User_Master.config.security.JwtAuthenticationEntryPoint;
import com.vijay.User_Master.config.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // 0) CORS preflight must be open
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 1) All common static resources (css/js/images/webjars/favicon, etc.)
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                        // 2) Your explicit static & root fallbacks (Vite assets etc.)
                        .requestMatchers(
                                "/", "/index.html",
                                "/assets/**", "/static/**",
                                "/favicon.ico", "/vite.svg",
                                "/manifest.*", "/robots.txt",
                                "/error"
                        ).permitAll()

                        // 3) Public endpoints
                        .requestMatchers(
                                "/api/auth/login",
                                "/login", "/signin",
                                "/api/chat/message",
                                "/api/chat/providers",
                                "/api/chat/providers/{providerName}/models",
                                "/api/auth/register/**",
                                "/api/v1/home/**",
                                "/api/v1/tokens/**",
                                "/api/users/image/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**"
                        ).permitAll()

                        // 4) Everything else needs JWT
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
