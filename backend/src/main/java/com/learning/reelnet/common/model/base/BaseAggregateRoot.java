package com.learning.reelnet.common.model.base;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.learning.reelnet.common.infrastructure.events.DomainEvent;

/**
 * Base class for all Aggregate Roots in the domain model.
 * In Domain-Driven Design, an Aggregate Root is a cluster of domain objects
 * that can be treated as a single unit.
 * It encapsulates and controls access to its constituent parts and is
 * responsible for ensuring the consistency
 * of the entire aggregate.
 * <p>
 * This base class provides common functionality for managing domain events
 * raised by the aggregate.
 * 
 * @param <ID> The type of the identifier
 */
@MappedSuperclass
public abstract class BaseAggregateRoot<ID extends Serializable> extends BaseEntity<ID> {

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * Registers a domain event to be published when the aggregate is saved.
     * This allows for capturing business events that occur within the aggregate
     * and can be used to trigger side effects or notify other parts of the system.
     *
     * @param event The domain event to register
     */
    protected void registerEvent(DomainEvent event) {
        if (event != null) {
            domainEvents.add(event);
        }
    }

    /**
     * Clears all domain events from this aggregate.
     * This should be called after the events have been dispatched to avoid
     * duplicate publications.
     */
    public void clearEvents() {
        domainEvents.clear();
    }

    /**
     * Returns an unmodifiable view of the domain events registered by this
     * aggregate.
     * 
     * @return An unmodifiable list of domain events
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Checks if this aggregate has any domain events registered.
     * 
     * @return true if there are domain events, false otherwise
     */
    public boolean hasDomainEvents() {
        return !domainEvents.isEmpty();
    }

    /**
     * Ensures that the aggregate is in a valid state according to invariants.
     * This method should be called before any state-changing operations.
     * It should throw an exception if the aggregate's invariants are violated.
     * 
     * @throws IllegalStateException if the aggregate is in an invalid state
     */
    protected abstract void validateInvariants();

    /**
     * Creates a snapshot of the aggregate's state.
     * This can be useful for event sourcing, or for creating a point-in-time view
     * of the aggregate.
     * 
     * @return A serializable representation of the aggregate's state
     */
    @Transient
    public abstract Serializable createSnapshot();

    /**
     * Reconstitutes an aggregate from a snapshot.
     * This is the counterpart to createSnapshot() and allows recreating the
     * aggregate from a saved state.
     * 
     * @param snapshot The snapshot to restore the aggregate from
     */
    protected abstract void applySnapshot(Serializable snapshot);
}