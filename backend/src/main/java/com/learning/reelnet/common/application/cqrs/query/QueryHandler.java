package com.learning.reelnet.common.application.cqrs.query;

/**
 * Interface for query handlers in CQRS pattern.
 * A query handler is responsible for processing a specific query type
 * and returning the requested information.
 * 
 * @param <R> The type of result returned by the query
 * @param <Q> The type of query to handle
 */
public interface QueryHandler<R, Q extends Query<R>> {
    /**
     * Handles the query and returns a result.
     * This method contains the logic for processing the query and retrieving data.
     * 
     * @param query The query to handle
     * @return The result of handling the query
     * @throws Exception If an error occurs during query processing
     */
    R handle(Q query) throws Exception;
    
    /**
     * Get the query class this handler can process.
     * Default implementation uses generic type inference.
     * 
     * @return The class of queries this handler can process
     */
    default Class<Q> getQueryClass() {
        throw new UnsupportedOperationException("Query handlers must override getQueryClass() method");
    }
} 