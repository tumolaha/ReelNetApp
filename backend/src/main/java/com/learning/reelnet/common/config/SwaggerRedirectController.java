package com.learning.reelnet.common.config;

import org.springframework.beans.factory.annotation.Value;
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
    
    @Value("${server.servlet.context-path:/api}")
    private String contextPath;
    
    /**
     * Redirect root URL to Swagger UI
     */
    @GetMapping("/doc")
    public RedirectView redirectToSwaggerUi() {
        return new RedirectView(contextPath + "/swagger-ui/index.html");
    }
    
    /**
     * Redirect from old swagger-ui.html path to new Swagger UI
     */
    @GetMapping("/swagger-ui.html")
    public RedirectView redirectFromOldSwaggerUiPath() {
        return new RedirectView(contextPath + "/swagger-ui/index.html");
    }
    
    /**
     * Redirect to the correct api-docs URL
     */
    @GetMapping("/api-docs")
    public RedirectView redirectApiDocsToCorrectPath() {
        return new RedirectView(contextPath + "/api-docs");
    }
    
    /**
     * Redirect to the correct swagger-config URL
     */
    @GetMapping("/api-docs/swagger-config")
    public RedirectView redirectSwaggerConfigToCorrectPath() {
        return new RedirectView(contextPath + "/api-docs/swagger-config");
    }

    
}