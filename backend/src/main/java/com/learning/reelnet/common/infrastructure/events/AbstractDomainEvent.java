package com.learning.reelnet.common.infrastructure.events;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * Base implementation for domain events.
 */
@Getter
public abstract class AbstractDomainEvent implements DomainEvent {
    
    private final String eventId = UUID.randomUUID().toString();
    private final String eventType;
    private final String aggregateId;
    private final long aggregateVersion;
    private final int eventVersion;
    private final Instant occurredOn = Instant.now();
    
    protected AbstractDomainEvent(String eventType, String aggregateId, long aggregateVersion, int eventVersion) {
        this.eventType = eventType;
        this.aggregateId = aggregateId;
        this.aggregateVersion = aggregateVersion;
        this.eventVersion = eventVersion;
    }

    @Override
    public boolean isPublishable() {
        return true; // By default, all events are publishable
    }

    @Override
    public Serializable toSerializable() {
        return this; // By default, the event itself is serializable
    }
} 