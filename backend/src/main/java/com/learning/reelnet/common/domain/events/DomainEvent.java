package com.learning.reelnet.common.domain.events;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * Base interface for all domain events in the system.
 * Domain events represent something meaningful that happened in the domain.
 * They are typically raised by aggregates and can be used to trigger side-effects,
 * notify other parts of the system, or for event sourcing.
 * <p>
 * Domain events should be immutable and capture the essence of the change that occurred.
 */
public interface DomainEvent extends Serializable {

    /**
     * Gets the unique identifier of this event.
     * 
     * @return The event's unique identifier
     */
    UUID getEventId();

    /**
     * Gets the type of this event.
     * Typically, this is the simple class name, but can be customized if needed.
     * 
     * @return The event type
     */
    String getEventType();

    /**
     * Gets the timestamp when this event occurred.
     * 
     * @return The event timestamp
     */
    Instant getOccurredOn();

    /**
     * Gets the version of this event.
     * This is useful for event versioning in event sourcing systems
     * or when the structure of the event changes over time.
     * 
     * @return The event version
     */
    int getVersion();

    /**
     * Gets additional metadata about this event.
     * This can include information such as the user who triggered the event,
     * the source system, or correlation IDs for distributed tracing.
     * 
     * @return The event metadata
     */
    EventMetadata getMetadata();
    
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