package com.learning.reelnet.common.application.cqrs.query;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Simple implementation of the QueryBus interface with basic caching.
 */
@Slf4j
@Component
public class SimpleQueryBus implements QueryBus {
    
    private final ApplicationContext applicationContext;
    private final CacheManager cacheManager;
    private final Map<Class<?>, QueryHandler<?, ?>> handlers = new ConcurrentHashMap<>();
    
    public SimpleQueryBus(ApplicationContext applicationContext, CacheManager cacheManager) {
        this.applicationContext = applicationContext;
        this.cacheManager = cacheManager;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <R, Q extends Query<R>> R dispatch(Q query) throws Exception {
        log.debug("Dispatching query: {}", query.getQueryName());
        
        // Check if query is cacheable
        if (query instanceof CacheableQuery) {
            CacheableQuery<R> cacheableQuery = (CacheableQuery<R>) query;
            String cacheKey = cacheableQuery.getCacheKey();
            String cacheName = cacheableQuery.getCacheName();
            
            // Try to get from cache
            if (cacheManager.getCache(cacheName) != null) {
                R cachedResult = cacheManager.getCache(cacheName).get(cacheKey, cacheableQuery.getResultClass());
                if (cachedResult != null) {
                    log.debug("Cache hit for query: {}", query.getQueryName());
                    return cachedResult;
                }
            }
            
            // Not in cache, execute and store result
            R result = executeQuery(query);
            
            // Store in cache if not null
            if (result != null && cacheManager.getCache(cacheName) != null) {
                cacheManager.getCache(cacheName).put(cacheKey, result);
            }
            
            return result;
        }
        
        // Regular query, no caching
        return executeQuery(query);
    }
    
    @Override
    @Async
    public <R, Q extends Query<R>> CompletableFuture<R> dispatchAsync(Q query) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return dispatch(query);
            } catch (Exception e) {
                throw new RuntimeException("Error dispatching query asynchronously", e);
            }
        });
    }
    
    @Override
    public <R, Q extends Query<R>> void register(QueryHandler<R, Q> handler) {
        Class<Q> queryClass;
        try {
            queryClass = handler.getQueryClass();
        } catch (UnsupportedOperationException e) {
            log.warn("Query handler {} did not implement getQueryClass(), skipping registration", 
                    handler.getClass().getSimpleName());
            return;
        }
        
        log.debug("Registering handler {} for query {}", 
                handler.getClass().getSimpleName(), 
                queryClass.getSimpleName());
                
        handlers.put(queryClass, handler);
    }
    
    private <R, Q extends Query<R>> R executeQuery(Q query) throws Exception {
        QueryHandler<R, Q> handler = findHandler(query);
        try {
            return handler.handle(query);
        } catch (Exception e) {
            log.error("Error handling query {}: {}", query.getQueryName(), e.getMessage(), e);
            throw e;
        }
    }
    
    @SuppressWarnings("unchecked")
    private <R, Q extends Query<R>> QueryHandler<R, Q> findHandler(Q query) {
        Class<?> queryType = query.getClass();
        
        // First check if we have a registered handler
        QueryHandler<R, Q> handler = (QueryHandler<R, Q>) handlers.get(queryType);
        
        // If not, try to find it in the application context
        if (handler == null) {
            String handlerBeanName = queryType.getSimpleName() + "Handler";
            try {
                handler = (QueryHandler<R, Q>) applicationContext.getBean(handlerBeanName);
                // Cache the handler for future use
                handlers.put(queryType, handler);
            } catch (Exception e) {
                throw new IllegalStateException(
                    "No handler registered for query " + query.getQueryName(), e);
            }
        }
        
        return handler;
    }
    
    /**
     * Interface for cacheable queries
     * @param <R> Result type
     */
    public interface CacheableQuery<R> extends Query<R> {
        /**
         * Get the cache key for this query
         * @return Cache key
         */
        String getCacheKey();
        
        /**
         * Get the cache name to use
         * @return Cache name
         */
        String getCacheName();
        
        /**
         * Get the result class
         * @return Class of the result
         */
        Class<R> getResultClass();
    }
} 