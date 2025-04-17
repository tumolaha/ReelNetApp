package com.learning.reelnet.common.infrastructure.messaging;

import com.learning.reelnet.common.infrastructure.events.DomainEvent;
import com.learning.reelnet.common.infrastructure.events.EventPublisher;
import com.learning.reelnet.common.model.base.BaseAggregateRoot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementation of the EventPublisher interface using Kafka.
 * This class is responsible for publishing domain events to Kafka topics.
 * Only active when Kafka is enabled.
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true")
public class EventPublisherImpl implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    @Value("${spring.kafka.topics.domain-events:domain-events}")
    private String domainEventsTopic;
    
    // Using CopyOnWriteArrayList for thread safety
    private final List<EventPublicationListener> listeners = new CopyOnWriteArrayList<>();

    public EventPublisherImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public <T extends DomainEvent> void publish(T event) {
        if (event == null || !event.isPublishable()) {
            return;
        }

        try {
            CompletableFuture<Void> future = publishToKafka(event);
            // Only notify listeners if publishing was successful
            future.thenRun(() -> notifyListeners(event))
                  .exceptionally(ex -> {
                      log.error("Failed to publish event: {}", event, ex);
                      return null;
                  });
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
            // Only clear events if publishing was successful
            aggregateRoot.clearEvents();
        } catch (Exception e) {
            log.error("Failed to publish events from aggregate: {}", aggregateRoot, e);
            throw new RuntimeException("Event publication from aggregate failed", e);
        }
    }

    @Override
    public boolean isReady() {
        try {
            // A simple check to see if Kafka is available
            return kafkaTemplate != null && kafkaTemplate.getDefaultTopic() != null;
        } catch (Exception e) {
            log.warn("Kafka is not ready: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void registerPublicationListener(EventPublicationListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Publishes an event to Kafka.
     *
     * @param event the event to publish
     * @param <T> the type of the event
     * @return a CompletableFuture that completes when the event is published
     */
    private <T extends DomainEvent> CompletableFuture<Void> publishToKafka(T event) {
        String key = event.getEventId().toString();
        
        return kafkaTemplate.send(domainEventsTopic, key, event.toSerializable())
                .thenApply(result -> {
                    log.debug("Published event: {} with offset: {}", 
                            event, result.getRecordMetadata().offset());
                    return null;
                });
    }

    /**
     * Notifies all registered listeners that an event has been published.
     *
     * @param event the published event
     */
    private <T extends DomainEvent> void notifyListeners(T event) {
        listeners.forEach(listener -> {
            try {
                listener.onEventPublished(event);
            } catch (Exception e) {
                log.error("Error notifying listener: {}", listener, e);
            }
        });
    }
}