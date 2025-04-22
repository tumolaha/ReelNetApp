package com.learning.reelnet.common.infrastructure.ratelimit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Configuration properties for rate limiting.
 */
@Data
@Primary
@ConfigurationProperties(prefix = "app.rate-limit")
public class RateLimitProperties {
    
    /**
     * Whether rate limiting is enabled
     */
    private boolean enabled = true;
    
    /**
     * The name of the cache to use for storing rate limit data
     */
    private String cacheName = "rateLimitCache";
    
    /**
     * The available rate limits for different endpoints or scenarios
     */
    private Map<String, BucketConfig> limits = new HashMap<>();
    
    /**
     * Configuration for a rate limit bucket
     */
    @Data
    public static class BucketConfig {
        /**
         * The total capacity of the bucket (maximum tokens)
         */
        private long capacity = 50;
        
        /**
         * The number of tokens to refill
         */
        private long refillTokens = 50;
        
        /**
         * The duration of the refill period
         */
        private long refillDuration = 1;
        
        /**
         * The time unit for the refill duration
         */
        private TimeUnit refillTimeUnit = TimeUnit.MINUTES;
    }
} 