package com.learning.reelnet.common.infrastructure.ratelimit;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for rate limiting.
 * Enables the rate limit properties and initializes the rate limiting system.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RateLimitProperties.class)
@ConditionalOnProperty(prefix = "app.rate-limit", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RateLimitConfig {

    private final RateLimitProperties properties;

    /**
     * Log information about rate limit configuration at startup
     */
    @PostConstruct
    public void init() {
        log.info("Rate limiting enabled: {}", properties.isEnabled());
        properties.getLimits().forEach((key, config) -> {
            log.info("Rate limit for '{}': {} tokens / {} {}",
                    key,
                    config.getCapacity(),
                    config.getRefillDuration(),
                    config.getRefillTimeUnit().toString().toLowerCase());
        });
    }
} 