package com.splitbuddy.settlement_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // disable CSRF for Postman
            .authorizeHttpRequests()
                .anyRequest().permitAll(); // allow all endpoints
        return http.build();
    }
}
