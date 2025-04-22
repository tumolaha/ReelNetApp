package com.learning.reelnet.common.application.cqrs.query;

import com.learning.reelnet.common.application.cqrs.query.SimpleQueryBus.CacheableQuery;

import lombok.Getter;


/**
 * Base class for cacheable queries.
 * Extends BaseQuery and implements CacheableQuery interface for caching support.
 *
 * @param <R> The type of result returned by the query
 */
@Getter
public abstract class BaseCacheableQuery<R> extends BaseQuery<R> implements CacheableQuery<R> {
    
    private final String cacheName;
    private final Class<R> resultClass;
    
    /**
     * Constructs a new BaseCacheableQuery
     *
     * @param cacheName Name of the cache to use
     * @param resultClass Class of the result for type-safe cache retrieval
     */
    protected BaseCacheableQuery(String cacheName, Class<R> resultClass) {
        super();
        this.cacheName = cacheName;
        this.resultClass = resultClass;
    }
    
    /**
     * Generates a cache key based on the query attributes.
     * Default implementation uses query name and toString.
     * Override this method to provide a more specific cache key.
     *
     * @return Cache key
     */
    @Override
    public String getCacheKey() {
        return getQueryName() + ":" + this.toString();
    }
} 