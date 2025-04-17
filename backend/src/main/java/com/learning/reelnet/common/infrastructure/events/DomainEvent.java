package com.learning.reelnet.common.infrastructure.events;

import java.io.Serializable;
import java.time.Instant;

/**
 * Base interface for all domain events in the system.
 * Domain events represent something significant that happened in the domain.
 * They are typically raised by aggregates and can be used to trigger side-effects,
 * notify other parts of the system, or for event sourcing.
 * <p>
 * Domain events should be immutable and capture the essence of the change that occurred.
 */
public interface DomainEvent extends Serializable {

    /**
     * Gets the unique identifier of this event.
     *
     * @return The event ID
     */
    String getEventId();

    /**
     * Gets the type of the event.
     * This should be a business-meaningful name.
     *
     * @return The event type
     */
    String getEventType();

    /**
     * Gets the aggregate ID that this event is associated with.
     *
     * @return The aggregate ID
     */
    String getAggregateId();

    /**
     * Gets the version of the aggregate when this event occurred.
     *
     * @return The aggregate version
     */
    long getAggregateVersion();

    /**
     * Gets the version of this event type.
     * This is useful for event versioning in event sourcing systems
     * or when the structure of the event changes over time.
     * 
     * @return The event schema version
     */
    int getEventVersion();

    /**
     * Gets the timestamp when this event occurred.
     *
     * @return The occurrence timestamp
     */
    Instant getOccurredOn();
    
    /**
     * Determines if this event can be published to external systems.
     * Some events might be intended for internal use only.
     * 
     * @return true if the event can be published externally, false otherwise
     */
    boolean isPublishable();
    
    /**
     * Converts the event to a serializable format suitable for persistence or messaging.
     * 
     * @return A serializable representation of the event
     */
    Serializable toSerializable();
} 