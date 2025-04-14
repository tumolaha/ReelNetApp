package com.learning.reelnet.common.domain.events;

import java.util.Collection;

import com.learning.reelnet.common.domain.base.BaseAggregateRoot;

/**
 * Interface responsible for publishing domain events.
 * This interface follows the ports and adapters pattern, where domain layer
 * defines
 * the port (this interface) and infrastructure layer provides the
 * implementation (adapter).
 * <p>
 * This decouples the domain model from the specific event publishing mechanism
 * (e.g., Kafka, RabbitMQ, in-memory) allowing for flexibility and testability.
 */
public interface EventPublisher {

    /**
     * Publishes a single domain event.
     * 
     * @param event The domain event to publish
     * @param <T>   The type of domain event
     */
    <T extends DomainEvent> void publish(T event);

    /**
     * Publishes multiple domain events in a batch.
     * The implementation should ensure that either all events are published
     * or none (transactional publishing).
     * 
     * @param events The collection of domain events to publish
     * @param <T>    The type of domain events
     */
    <T extends DomainEvent> void publishAll(Collection<T> events);

    /**
     * Publishes all domain events from an aggregate root.
     * After publication, the events should be cleared from the aggregate.
     * 
     * @param aggregateRoot The aggregate root containing events to publish
     * @param <ID>          The type of the aggregate's identifier
     */
    <ID extends java.io.Serializable> void publishEventsFrom(BaseAggregateRoot<ID> aggregateRoot);

    /**
     * Checks if the publisher is ready to publish events.
     * This can be used to check the health of the underlying messaging system.
     * 
     * @return true if the publisher is ready, false otherwise
     */
    boolean isReady();

    /**
     * Registers a listener to be notified after events are published.
     * This can be useful for testing or for executing post-publication logic.
     * 
     * @param listener The event publication listener
     */
    void registerPublicationListener(EventPublicationListener listener);

    /**
     * Listener interface for post-publication notifications.
     */
    interface EventPublicationListener {
        /**
         * Called after a domain event has been published.
         * 
         * @param event The published domain event
         */
        void onEventPublished(DomainEvent event);
    }
}