package com.learning.reelnet.common.application.cqrs.query;

import java.io.Serializable;

/**
 * Base interface for all query objects in the application.
 * Queries represent a request for information without changing the state of the system.
 * <p>
 * In the Query pattern (part of CQRS), queries are sent to a query handler
 * that processes them and returns the requested data.
 * <p>
 * Queries should:
 * - Be named with a verb that implies reading or getting information (e.g., GetUser, FindOrders)
 * - Contain all the parameters needed to perform the query
 * - Be immutable
 *
 * @param <R> the type of result expected from this query
 */
public interface Query<R> extends Serializable {

    /**
     * Gets the query identifier.
     * This can be used for logging, tracing, or caching.
     *
     * @return the query identifier
     */
    String getQueryId();

    /**
     * Gets the name of the query.
     * By default, this is the simple name of the query class.
     *
     * @return the query name
     */
    default String getQueryName() {
        return this.getClass().getSimpleName();
    }
} 