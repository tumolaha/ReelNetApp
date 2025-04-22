package com.learning.reelnet.common.application.cqrs.query;

import java.util.concurrent.CompletableFuture;

/**
 * Interface for query bus in CQRS pattern.
 * The query bus is responsible for routing queries to their appropriate handlers
 * and can provide cross-cutting concerns like caching, logging, and metrics.
 */
public interface QueryBus {
    /**
     * Dispatches a query to its appropriate handler.
     * This is a synchronous operation that will block until the query is handled.
     * 
     * @param <R> The type of result returned by the query
     * @param <Q> The type of query to dispatch
     * @param query The query to dispatch
     * @return The result of handling the query
     * @throws Exception If an error occurs during query handling
     */
    <R, Q extends Query<R>> R dispatch(Q query) throws Exception;
    
    /**
     * Dispatches a query asynchronously.
     * This is an asynchronous operation that will return immediately.
     * 
     * @param <R> The type of result returned by the query
     * @param <Q> The type of query to dispatch
     * @param query The query to dispatch
     * @return A CompletableFuture that will be completed with the result or an exception
     */
    <R, Q extends Query<R>> CompletableFuture<R> dispatchAsync(Q query);
    
    /**
     * Registers a query handler with this query bus.
     * 
     * @param <R> The type of result returned by the query
     * @param <Q> The type of query to handle
     * @param handler The query handler to register
     */
    <R, Q extends Query<R>> void register(QueryHandler<R, Q> handler);
} 