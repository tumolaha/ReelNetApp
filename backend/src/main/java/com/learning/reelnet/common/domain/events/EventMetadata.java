package com.learning.reelnet.common.domain.events;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Container for metadata associated with domain events.
 * This can include information such as the user who triggered the event,
 * correlation IDs for distributed tracing, or any other contextual information.
 */
@Getter
@ToString
@Builder
public class EventMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The user who triggered the event, if applicable.
     */
    private final String userId;

    /**
     * The source system or component that generated the event.
     */
    private final String source;

    /**
     * Correlation ID for distributed tracing.
     */
    private final String correlationId;

    /**
     * Additional custom attributes.
     */
    @Builder.Default
    private final Map<String, Object> attributes = new HashMap<>();

    /**
     * Adds a custom attribute to the metadata.
     * 
     * @param key The attribute key
     * @param value The attribute value
     * @return This metadata instance for chaining
     */
    public EventMetadata addAttribute(String key, Object value) {
        attributes.put(key, value);
        return this;
    }

    /**
     * Gets a custom attribute by key.
     * 
     * @param key The attribute key
     * @return The attribute value, or null if not found
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * Checks if the metadata contains a specific attribute.
     * 
     * @param key The attribute key
     * @return true if the attribute exists, false otherwise
     */
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }
}