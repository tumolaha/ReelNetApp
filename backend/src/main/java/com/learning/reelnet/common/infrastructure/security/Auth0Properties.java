package com.learning.reelnet.common.infrastructure.security;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
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
    @Value("${auth0.domain:your-tenant.auth0.com}")
    private String domain;

    /**
     * Auth0 audience (API identifier)
     */
    @Value("${auth0.audience:your-api-identifier}")
    private String audience;

    /**
     * Auth0 client ID
     */
    @Value("${auth0.client-id:your-client-id}")
    private String clientId;

    /**
     * Auth0 client secret
     */
    @Value("${auth0.client-secret:your-client-secret}")
    private String clientSecret;

    /**
     * Get formatted issuer URI for token validation
     */
    public String getIssuerUri() {
        return String.format("https://%s/", domain);
    }

    /**
     * Get JWK Set URI for token validation
     */
    public String getJwkSetUri() {
        return String.format("https://%s/.well-known/jwks.json", domain);
    }
}