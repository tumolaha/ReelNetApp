package com.learning.reelnet.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller to handle Swagger UI redirects and ensure it works with the context path.
 */
@Controller
@Configuration
public class SwaggerRedirectController {

    /**
     * Redirect to the correct swagger-config URL
     */
    @GetMapping("/api-docs/swagger-config")
    public RedirectView redirectSwaggerConfigToCorrectPath() {
        return new RedirectView("/api/v1/api-docs/swagger-config");
    }

    /**
     * Redirect to the correct api-docs URL
     */
    @GetMapping("/api-docs")
    public RedirectView redirectApiDocsToCorrectPath() {
        return new RedirectView("/api/v1/api-docs");
    }

    /**
     * Redirect API docs requests that include the context path
     */
    @GetMapping("/swagger-ui/api/v1/api-docs")
    public RedirectView redirectApiDocs() {
        return new RedirectView("/api/v1/api-docs");
    }
    
    /**
     * Redirect swagger config requests that include the context path
     */
    @GetMapping("/swagger-ui/api/v1/api-docs/swagger-config")
    public RedirectView redirectSwaggerConfig() {
        return new RedirectView("/api/v1/api-docs/swagger-config");
    }

    /**
     * Allow public access to Swagger UI and API docs
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain swaggerPublicSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/swagger-ui/**", "/api-docs/**", "/", "/api/v1/swagger-ui/**", "/api/v1/api-docs/**")
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll()
            );
        return http.build();
    }
} 