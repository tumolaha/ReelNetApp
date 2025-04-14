package com.learning.reelnet.common.domain.events;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * Base implementation of the DomainEvent interface.
 * Provides common functionality for all domain events.
 */
@Getter
public abstract class AbstractDomainEvent implements DomainEvent {

    private static final long serialVersionUID = 1L;

    private final UUID eventId;
    private final Instant occurredOn;
    private final int version;
    private final EventMetadata metadata;

    /**
     * Creates a new domain event with the given metadata.
     * 
     * @param metadata The event metadata
     */
    protected AbstractDomainEvent(EventMetadata metadata) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = Instant.now();
        this.version = 1;
        this.metadata = metadata != null ? metadata : EventMetadata.builder().build();
    }

    /**
     * Creates a new domain event with empty metadata.
     */
    protected AbstractDomainEvent() {
        this(null);
    }

    @Override
    public String getEventType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isPublishable() {
        return true;
    }

    @Override
    public Serializable toSerializable() {
        return this;
    }

    @Override
    public String toString() {
        return "DomainEvent{" +
                "type='" + getEventType() + '\'' +
                ", id=" + eventId +
                ", occurredOn=" + occurredOn +
                ", version=" + version +
                ", metadata=" + metadata +
                '}';
    }
}