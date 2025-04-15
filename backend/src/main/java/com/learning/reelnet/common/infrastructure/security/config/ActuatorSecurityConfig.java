package com.learning.reelnet.common.infrastructure.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration specific to Actuator endpoints.
 */
@Configuration
public class ActuatorSecurityConfig {

    @Bean
    @Order(1) // Higher priority than the main security config
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/actuator/**")
            .authorizeHttpRequests(authorize -> authorize
                // Publicly accessible endpoints
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                // Secured endpoints requiring authentication
                .requestMatchers("/actuator/**").hasAuthority("SCOPE_admin:system")
            )
            .csrf(AbstractHttpConfigurer::disable);
        
        return http.build();
    }
}