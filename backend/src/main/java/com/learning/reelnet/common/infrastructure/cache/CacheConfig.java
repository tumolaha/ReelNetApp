package com.learning.reelnet.common.infrastructure.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * Cache configuration for the application.
 * This class configures caching using Redis in production and a simple in-memory cache in development.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Cache names for the application.
     * These constants define the available caches.
     */
    public static final String USER_CACHE = "userCache";
    public static final String EXAM_CACHE = "examCache";
    public static final String VOCABULARY_CACHE = "vocabularyCache";

    /**
     * Creates a Redis cache manager for production.
     *
     * @param connectionFactory the Redis connection factory
     * @return the Redis cache manager
     */
    @Bean
    @Profile("!dev")
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // Configure the user cache with a 30-minute TTL
        cacheConfigurations.put(USER_CACHE, createCacheConfiguration(Duration.ofMinutes(30)));
        
        // Configure the exam cache with a 1-hour TTL
        cacheConfigurations.put(EXAM_CACHE, createCacheConfiguration(Duration.ofHours(1)));
        
        // Configure the vocabulary cache with a 2-hour TTL
        cacheConfigurations.put(VOCABULARY_CACHE, createCacheConfiguration(Duration.ofHours(2)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(createCacheConfiguration(Duration.ofMinutes(10)))
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    /**
     * Creates a simple in-memory cache manager for development.
     *
     * @return the in-memory cache manager
     */
    @Bean
    @Profile("dev")
    public CacheManager simpleCacheManager() {
        return new ConcurrentMapCacheManager(USER_CACHE, EXAM_CACHE, VOCABULARY_CACHE);
    }

    /**
     * Creates a Redis cache configuration with the specified TTL.
     *
     * @param ttl the time-to-live duration
     * @return the Redis cache configuration
     */
    private RedisCacheConfiguration createCacheConfiguration(Duration ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .recordStats());
        
        // Đặt tên cho các cache
        cacheManager.setCacheNames(List.of(
            "userSyncCache", 
            "userByIdCache", 
            "rolesByNameCache",
            "permissionsByNameCache"
        ));
        
        return cacheManager;
    }
    
    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(60, TimeUnit.MINUTES);
    }
}