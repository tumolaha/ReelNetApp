package com.learning.reelnet.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.learning.reelnet.common.infrastructure.security.Auth0Properties;
import com.learning.reelnet.common.infrastructure.security.handler.CustomAuthenticationEntryPoint;
import com.learning.reelnet.common.infrastructure.security.helper.JwtDecoderLoggingFilter;
import com.learning.reelnet.common.infrastructure.security.filter.UserSynchronizationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Security configuration for the application.
 * This class configures Spring Security with Auth0 JWT validation.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

        private static final String[] PUBLIC_URLS = {
                        "/api/public/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/api-docs/**",
                        "/",
                        "/actuator/health",
                        "/actuator/info"
        };
        
        private final Auth0Properties auth0Properties;
        private final CorsFilter corsFilter;
        private final JwtDecoderLoggingFilter jwtLoggingFilter;
        private final CustomAuthenticationEntryPoint authenticationEntryPoint;
        private final Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter;

        @Bean
        @Profile("!dev")
        public SecurityFilterChain productionSecurityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(cors -> {})
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers(PUBLIC_URLS).permitAll()
                                                .requestMatchers("/actuator/**").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(authenticationEntryPoint))
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwt -> jwt
                                                                .decoder(jwtDecoder())
                                                                .jwtAuthenticationConverter(jwtAuthenticationConverter))
                                                .authenticationEntryPoint(authenticationEntryPoint))
                                .addFilter(corsFilter)
                                .addFilterBefore(jwtLoggingFilter, BasicAuthenticationFilter.class)
                                .headers(headers -> headers
                                                .frameOptions(frameOptions -> frameOptions.sameOrigin())
                                                .xssProtection(xss -> xss.disable())
                                                .contentSecurityPolicy(
                                                                csp -> csp.policyDirectives("frame-ancestors 'self'")));

                return http.build();
        }

        @Bean
        @Profile("dev")
        public SecurityFilterChain developmentSecurityFilterChain(HttpSecurity http) throws Exception {
                // In development mode, we can use less restrictive security settings
                http
                                .cors(cors -> {})
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(authorize -> authorize
                                                // In dev mode, we can allow all requests or modify for your needs
                                                .requestMatchers(PUBLIC_URLS).permitAll()
                                                .anyRequest().authenticated())
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(authenticationEntryPoint))
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwt -> jwt
                                                                .decoder(jwtDecoder())
                                                                .jwtAuthenticationConverter(jwtAuthenticationConverter))
                                                .authenticationEntryPoint(authenticationEntryPoint))
                                .addFilter(corsFilter)
                                .addFilterBefore(jwtLoggingFilter, BasicAuthenticationFilter.class)
                                .headers(headers -> headers
                                                .frameOptions(frameOptions -> frameOptions.sameOrigin())
                                                .xssProtection(xss -> xss.disable())
                                                .contentSecurityPolicy(
                                                                csp -> csp.policyDirectives("frame-ancestors 'self'")));

                return http.build();
        }

        @Bean
        public JwtDecoder jwtDecoder() {

                try {
                        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(auth0Properties.getJwkSetUri())
                                        .build();
                        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators
                                        .createDefaultWithIssuer(auth0Properties.getIssuerUri());
                        jwtDecoder.setJwtValidator(withIssuer);

                        return jwtDecoder;
                } catch (Exception e) {
                        log.error("Error creating JWT Decoder", e);
                        throw e;
                }
        }
}