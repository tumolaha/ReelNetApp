package com.learning.reelnet.common.infrastructure.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Validator for validating the audience claim in JWT tokens from Auth0.
 */
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final String audience;
    private final OAuth2Error error = new OAuth2Error("invalid_token", "The token was not issued for the required audience", null);

    /**
     * Creates a new audience validator.
     *
     * @param audience the expected audience
     */
    public AudienceValidator(String audience) {
        this.audience = audience;
    }

    /**
     * Validates if the JWT token contains the expected audience.
     *
     * @param jwt the JWT token to validate
     * @return the validation result
     */
    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (jwt.getAudience().contains(audience)) {
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(error);
    }
} 