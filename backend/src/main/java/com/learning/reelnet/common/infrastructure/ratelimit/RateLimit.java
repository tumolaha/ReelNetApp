package com.learning.reelnet.common.infrastructure.ratelimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to apply rate limiting to controller methods.
 * This can be used to specify customized rate limits for specific endpoints.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    
    /**
     * The type of rate limit to apply. Should correspond to a configured limit in application.yml.
     */
    String type() default "default";
    
    /**
     * The key source to use for rate limiting.
     * IP: Use the client's IP address.
     * PRINCIPAL: Use the authenticated user's principal.
     * COMBINED: Use both IP and principal.
     */
    KeySource keySource() default KeySource.IP;
    
    /**
     * Sources that can be used for the rate limit key.
     */
    enum KeySource {
        IP,
        PRINCIPAL,
        COMBINED
    }
} 