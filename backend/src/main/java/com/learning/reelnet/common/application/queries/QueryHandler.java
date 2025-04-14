package com.learning.reelnet.common.application.queries;

/**
 * Base interface for all query handlers in the application.
 * Query handlers are responsible for processing queries and returning
 * the requested data without changing the state of the system.
 * <p>
 * In the Query pattern (part of CQRS), each query type typically has
 * a corresponding query handler that knows how to process it.
 *
 * @param <Q> the type of query this handler can process
 * @param <R> the type of result produced by handling the query
 */
public interface QueryHandler<Q extends Query<R>, R> {

    /**
     * Handles the given query and produces a result.
     *
     * @param query the query to handle
     * @return the result of handling the query
     * @throws Exception if an error occurs while handling the query
     */
    R handle(Q query) throws Exception;
}