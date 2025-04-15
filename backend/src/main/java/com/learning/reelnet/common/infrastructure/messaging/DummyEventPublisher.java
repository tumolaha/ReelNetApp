package com.learning.reelnet.common.infrastructure.messaging;

import com.learning.reelnet.common.domain.base.BaseAggregateRoot;
import com.learning.reelnet.common.domain.events.DomainEvent;
import com.learning.reelnet.common.domain.events.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A dummy implementation of EventPublisher that doesn't use Kafka.
 * This simply logs events and notifies listeners.
 */
@Component
@Primary
@Slf4j
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "false", matchIfMissing = true)
public class DummyEventPublisher implements EventPublisher {

    // Using CopyOnWriteArrayList for thread safety
    private final List<EventPublicationListener> listeners = new CopyOnWriteArrayList<>();

    @Override
    public <T extends DomainEvent> void publish(T event) {
        if (event == null || !event.isPublishable()) {
            return;
        }

        try {
            log.info("Dummy publisher: event logged: {}", event);
            notifyListeners(event);
        } catch (Exception e) {
            log.error("Failed to publish event: {}", event, e);
            throw new RuntimeException("Event publication failed", e);
        }
    }

    @Override
    public <T extends DomainEvent> void publishAll(Collection<T> events) {
        if (events == null || events.isEmpty()) {
            return;
        }

        events.stream()
                .filter(DomainEvent::isPublishable)
                .forEach(this::publish);
    }

    @Override
    public <ID extends Serializable> void publishEventsFrom(BaseAggregateRoot<ID> aggregateRoot) {
        if (aggregateRoot == null || !aggregateRoot.hasDomainEvents()) {
            return;
        }

        try {
            Collection<DomainEvent> events = aggregateRoot.getDomainEvents();
            publishAll(events);
            // Clear events after publishing
            aggregateRoot.clearEvents();
        } catch (Exception e) {
            log.error("Failed to publish events from aggregate: {}", aggregateRoot, e);
            throw new RuntimeException("Event publication from aggregate failed", e);
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void registerPublicationListener(EventPublicationListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Notifies all registered listeners that an event has been published.
     *
     * @param event the published event
     */
    private <T extends DomainEvent> void notifyListeners(T event) {
        for (EventPublicationListener listener : listeners) {
            try {
                listener.onEventPublished(event);
            } catch (Exception e) {
                log.error("Error notifying listener: {}", listener, e);
            }
        }
    }
} 