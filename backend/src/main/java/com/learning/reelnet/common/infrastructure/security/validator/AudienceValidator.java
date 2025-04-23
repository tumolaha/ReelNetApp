package com.learning.reelnet.common.infrastructure.security.validator;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
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
    
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;
    
    @Value("${app.security.jwt.disable-audience-check:false}")
    private boolean disableAudienceCheck;

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
     * In development environment or if explicitly configured, this validation can be relaxed.
     *
     * @param jwt the JWT token to validate
     * @return the validation result
     */
    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        List<String> tokenAudiences = jwt.getAudience();
        
        if (disableAudienceCheck || "dev".equalsIgnoreCase(activeProfile)) {
            return OAuth2TokenValidatorResult.success();
        }
        if (tokenAudiences.contains(audience)) {
            return OAuth2TokenValidatorResult.success();
        }
        if (audience.contains(",")) {
            List<String> acceptedAudiences = Arrays.asList(audience.split(","));
            for (String tokenAud : tokenAudiences) {
                if (acceptedAudiences.contains(tokenAud)) {
                    return OAuth2TokenValidatorResult.success();
                }
            }
        }
        return OAuth2TokenValidatorResult.failure(error);
    }
} 