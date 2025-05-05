package com.learning.reelnet.common.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.learning.reelnet.common.infrastructure.security.filter.JwtUserSyncAuthenticationConverter;

import lombok.AllArgsConstructor;

/**
 * Security Configuration
 * 
 * Defines security rules, authentication mechanisms, and authorization policies
 * for the ReelNet application. This configuration follows industry standard best
 * practices for securing Spring Boot applications.
 */
@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    private final JwtUserSyncAuthenticationConverter jwtUserSyncAuthenticationConverter;
    /**
     * Configures the main security filter chain
     * 
     * @param http HttpSecurity to configure
     * @return The configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configure CSRF protection
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                // Disable CSRF for API endpoints that are designed to be called by non-browser clients
                // .ignoringRequestMatchers("/api/v1/auth/**", "/webhook/**")
            )
            
            // Configure CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Configure security headers
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives(
                    "default-src 'self'; " +
                    "img-src 'self' data: https:; " +
                    "script-src 'self'; " +
                    "style-src 'self' 'unsafe-inline'; " +
                    "frame-ancestors 'none'"))
                .referrerPolicy(referrer -> referrer
                    .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                .frameOptions(frame -> frame.deny())
                .xssProtection(xss -> xss.disable()) // Modern browsers use CSP instead
            )
            
            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints - no authentication required
                // .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                // .requestMatchers("/api/v1/auth/**", "/api/v1/public/**").permitAll()
                // .requestMatchers("/error").permitAll()
                
                // Admin endpoints
                // .requestMatchers("/admin/**", "/api/v1/admin/**").hasRole("ADMIN")
                
                // Content manager endpoints
                // .requestMatchers("/api/v1/content-management/**").hasAnyRole("ADMIN", "CONTENT_MANAGER")
                
                // User management endpoints
                // .requestMatchers("/api/v1/users/**").hasAnyRole("ADMIN", "USER_MANAGER")
                
                // Reporting endpoints
                // .requestMatchers("/api/v1/reports/**").hasAnyRole("ADMIN", "ANALYST")
                
                // All other endpoints require authentication
                .anyRequest().authenticated()
            )
            
            // Configure session management (stateless for RESTful APIs)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Configure JWT authentication
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtUserSyncAuthenticationConverter)
                )
            );
        
        return http.build();
    }
    
    /**
     * Configures CORS (Cross-Origin Resource Sharing) settings
     * 
     * @return CorsConfigurationSource for the application
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Define allowed origins - replace with actual frontend URLs for production
        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000",
            "https://reelnet-frontend.example.com"
        ));
        
        // Define allowed methods
        configuration.setAllowedMethods(Arrays.asList(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.PATCH.name(),
            HttpMethod.DELETE.name(),
            HttpMethod.OPTIONS.name()
        ));
        
        // Define allowed headers
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
            "X-CSRF-Token"
        ));
        
        // Expose headers to the client
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "X-CSRF-Token"
        ));
        
        // Allow credentials
        configuration.setAllowCredentials(true);
        
        // Max age for CORS preflight requests
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    
    /**
     * Configures role hierarchy for more concise authorization rules
     * ADMIN > CONTENT_MANAGER > USER_MANAGER > USER
     * 
     * @return RoleHierarchy that defines the role inheritance
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = 
            "ROLE_ADMIN > ROLE_CONTENT_MANAGER\n" +
            "ROLE_ADMIN > ROLE_USER_MANAGER\n" +
            "ROLE_ADMIN > ROLE_ANALYST\n" +
            "ROLE_CONTENT_MANAGER > ROLE_USER\n" + 
            "ROLE_USER_MANAGER > ROLE_USER";
        
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }
}