package com.learning.reelnet.common.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Configuration for Cross-Origin Resource Sharing (CORS).
 * This is the sole CORS configuration for the application and should be used instead of
 * WebMvcConfigurer.addCorsMappings() as it provides more flexibility and applies earlier in the filter chain.
 * <p>
 * CORS settings can be configured in application.properties with the following properties:
 * <ul>
 *   <li>cors.allowed-origins</li>
 *   <li>cors.allowed-methods</li>
 *   <li>cors.allowed-headers</li>
 *   <li>cors.exposed-headers</li>
 *   <li>cors.max-age</li>
 *   <li>cors.allow-credentials</li>
 * </ul>
 */
@Configuration
@Slf4j // Tự động tạo logger
public class CorsConfig {

    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:8080}")
    private List<String> allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private List<String> allowedMethods;

    @Value("${cors.allowed-headers:*}")
    private List<String> allowedHeaders;

    @Value("${cors.exposed-headers:Authorization,Content-Disposition}")
    private List<String> exposedHeaders;

    @Value("${cors.max-age:3600}")
    private long maxAge;

    @Value("${cors.allow-credentials:true}")
    private boolean allowCredentials;

    /**
     * Creates a CORS filter with the configured CORS settings.
     * This filter will be applied to all requests before they reach the controllers.
     *
     * @return the CORS filter
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Set allowed origins
        config.setAllowedOrigins(allowedOrigins);
        
        // Set allowed HTTP methods
        config.setAllowedMethods(allowedMethods);
        
        // Set allowed headers
        config.setAllowedHeaders(allowedHeaders);
        
        // Set exposed headers
        config.setExposedHeaders(exposedHeaders);
        
        // Set max age for preflight requests
        config.setMaxAge(maxAge);
        
        // Allow credentials (cookies, authorization headers)
        config.setAllowCredentials(allowCredentials);

        // Apply the CORS configuration to all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}