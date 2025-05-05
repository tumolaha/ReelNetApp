package com.learning.reelnet.common.infrastructure.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtConfig {

    @Value("${auth0.domain}")
    private String domain;
    
    @Value("${auth0.audience}")
    private String audience;
    
    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkSetUri = String.format("https://%s/.well-known/jwks.json", domain);
        
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        
        // Thêm validators để kiểm tra issuer và audience
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> issuerValidator = JwtValidators.createDefaultWithIssuer(
                String.format("https://%s/", domain));
        OAuth2TokenValidator<Jwt> combinedValidators = new DelegatingOAuth2TokenValidator<>(
                audienceValidator, issuerValidator);
        
        jwtDecoder.setJwtValidator(combinedValidators);
        
        return jwtDecoder;
    }
    
    // Validator để kiểm tra audience
    static class AudienceValidator implements OAuth2TokenValidator<Jwt> {
        private final String audience;
        
        AudienceValidator(String audience) {
            this.audience = audience;
        }
        
        @Override
        public OAuth2TokenValidatorResult validate(Jwt jwt) {
            List<String> audiences = jwt.getAudience();
            if (audiences.contains(audience)) {
                return OAuth2TokenValidatorResult.success();
            }
            return OAuth2TokenValidatorResult.failure(
                new OAuth2Error("invalid_token", "Invalid audience", null));
        }
    }
}