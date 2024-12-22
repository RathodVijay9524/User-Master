package com.vijay.User_Master.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {
    @Bean
    public AuditorConfig auditAware(){
        return new AuditorConfig();
    }
}
