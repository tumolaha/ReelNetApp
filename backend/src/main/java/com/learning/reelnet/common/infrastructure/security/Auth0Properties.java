package com.learning.reelnet.common.infrastructure.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for Auth0 integration.
 */
@Data
@Component
@ConfigurationProperties(prefix = "auth0")
public class Auth0Properties {

    /**
     * Auth0 domain (e.g., your-tenant.auth0.com)
     */
    private String domain;

    /**
     * Auth0 audience (API identifier)
     */
    private String audience;

    /**
     * Auth0 client ID
     */
    private String clientId;

    /**
     * Auth0 client secret
     */
    private String clientSecret;

    /**
     * JWT issuer URI for token validation
     */
    private String issuer;
} 