package com.learning.reelnet.common.infrastructure.ratelimit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service to manage rate limiting.
 * This implementation uses a simple token bucket algorithm without dependencies.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitService {
    
    private final RateLimitProperties properties;
    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    
    /**
     * Check if a request should be rate limited.
     *
     * @param key The key to identify the rate limit (e.g., IP address, user ID, etc.)
     * @param limitType The type of limit to apply (from configuration)
     * @return true if the request is allowed, false if it's rate limited
     */
    public boolean allowRequest(String key, String limitType) {
        if (!properties.isEnabled()) {
            return true;
        }
        
        TokenBucket bucket = getBucket(key, limitType);
        return bucket.tryConsume(1);
    }
    
    /**
     * Get the remaining tokens for a specific key and limit type.
     *
     * @param key The key to identify the rate limit
     * @param limitType The type of limit to check
     * @return The number of remaining tokens
     */
    public long getRemainingTokens(String key, String limitType) {
        if (!properties.isEnabled()) {
            return Long.MAX_VALUE;
        }
        
        TokenBucket bucket = getBucket(key, limitType);
        return bucket.getAvailableTokens();
    }
    
    /**
     * Get or create a TokenBucket for the given key and limit type.
     *
     * @param key The key to identify the rate limit
     * @param limitType The type of limit to apply
     * @return The bucket for the key and limit type
     */
    private TokenBucket getBucket(String key, String limitType) {
        String bucketKey = key + ":" + limitType;
        return buckets.computeIfAbsent(bucketKey, k -> createBucket(limitType));
    }
    
    /**
     * Create a new bucket based on the limit type configuration.
     *
     * @param limitType The type of limit to create a bucket for
     * @return A new bucket
     */
    private TokenBucket createBucket(String limitType) {
        RateLimitProperties.BucketConfig config = properties.getLimits().get(limitType);
        
        // If the specified limit type doesn't exist, use the default
        if (config == null) {
            config = properties.getLimits().get("default");
            log.warn("Rate limit type '{}' not found, using default configuration", limitType);
            
            // If there's no default configuration, create a basic one
            if (config == null) {
                config = new RateLimitProperties.BucketConfig();
                log.warn("Default rate limit configuration not found, using built-in defaults");
            }
        }
        
        Duration refillPeriod = Duration.ofSeconds(config.getRefillTimeUnit().toSeconds(config.getRefillDuration()));
        return new TokenBucket(config.getCapacity(), config.getRefillTokens(), refillPeriod);
    }
    
    /**
     * Simple in-memory token bucket implementation.
     */
    private static class TokenBucket {
        private final long capacity;
        private final long refillTokens;
        private final Duration refillPeriod;
        private final AtomicInteger tokens;
        private Instant lastRefillTime;
        
        TokenBucket(long capacity, long refillTokens, Duration refillPeriod) {
            this.capacity = capacity;
            this.refillTokens = refillTokens;
            this.refillPeriod = refillPeriod;
            this.tokens = new AtomicInteger((int) capacity);
            this.lastRefillTime = Instant.now();
        }
        
        synchronized boolean tryConsume(int tokensToConsume) {
            refillIfNeeded();
            
            if (tokens.get() >= tokensToConsume) {
                tokens.addAndGet(-tokensToConsume);
                return true;
            }
            
            return false;
        }
        
        synchronized int getAvailableTokens() {
            refillIfNeeded();
            return tokens.get();
        }
        
        private void refillIfNeeded() {
            Instant now = Instant.now();
            long elapsedNanos = Duration.between(lastRefillTime, now).toNanos();
            long refillIntervalNanos = refillPeriod.toNanos();
            
            if (elapsedNanos >= refillIntervalNanos) {
                int periods = (int) (elapsedNanos / refillIntervalNanos);
                int tokensToAdd = (int) (periods * refillTokens);
                
                if (tokensToAdd > 0) {
                    int newTokens = Math.min(tokens.get() + tokensToAdd, (int) capacity);
                    tokens.set(newTokens);
                    lastRefillTime = now;
                }
            }
        }
    }
} 