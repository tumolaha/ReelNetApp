package com.learning.reelnet.common.domain.base;

import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

/**
 * Base abstract class for value objects.
 * In Domain-Driven Design, value objects are immutable objects that describe a concept 
 * or measurement in the domain with no conceptual identity.
 * <p>
 * Characteristics of value objects:
 * - Immutability: They should not be changed after creation
 * - Equality: They are compared by their value, not by identity
 * - Self-validation: They ensure their internal state is valid
 * - Side-effect free: Their methods do not change state, they return new instances
 * <p>
 * Note: All value objects extending this class should:
 * 1. Be final to enforce immutability
 * 2. Have all fields final
 * 3. Initialize all fields in the constructor
 * 4. Provide no setters
 * 5. Override equals and hashCode based on all properties
 */
@MappedSuperclass
public abstract class BaseValueObject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Validates that this value object is in a valid state.
     * This method should be called in the constructor of all value objects.
     * It should throw an exception if the value object is invalid.
     * 
     * @throws IllegalArgumentException if the value object is invalid
     */
    protected abstract void validate();

    /**
     * Determines if this value object is semantically equal to another object.
     * Value objects are compared by their properties, not by identity.
     * 
     * Note: This method is implemented by Lombok's @EqualsAndHashCode annotation,
     * but can be overridden if custom equality logic is needed.
     */
    @Override
    public abstract boolean equals(Object o);

    /**
     * Returns a hash code value for this value object.
     * Value objects with the same properties should have the same hash code.
     * 
     * Note: This method is implemented by Lombok's @EqualsAndHashCode annotation,
     * but can be overridden if custom hashing logic is needed.
     */
    @Override
    public abstract int hashCode();

    /**
     * Method to copy value object with modifications.
     * To maintain immutability, this method should return a new instance with the modified values,
     * rather than modifying the existing instance.
     * 
     * This is a marker method that subclasses should implement if they need
     * to provide a way to create modified copies.
     * 
     * @return a new instance of the value object with modified values
     */
    protected abstract BaseValueObject copy();
    
    /**
     * Returns a string representation of the value object.
     * 
     * Note: This method is implemented by Lombok's @ToString annotation,
     * but can be overridden if custom string representation is needed.
     */
    @Override
    public abstract String toString();
    
    /**
     * Checks if this value object is semantically equal to another value object.
     * This is a convenience method that avoids casting in subclasses.
     * 
     * @param other the other value object
     * @return true if the value objects are equal, false otherwise
     */
    protected boolean sameValueAs(BaseValueObject other) {
        return equals(other);
    }
}