package com.learning.reelnet.common.infrastructure.persistence;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for building JPA specifications.
 * Makes it easier to compose complex database queries in a type-safe manner.
 */
public abstract class BaseSpecification<T> implements Specification<T> {

    @Override
    public Predicate toPredicate(@Nullable Root<T> root, @Nullable CriteriaQuery<?> query, @Nullable CriteriaBuilder cb) {
        return createPredicate(root, query, cb);
    }

    /**
     * Creates a predicate for this specification.
     *
     * @param root  the root entity
     * @param query the criteria query
     * @param cb    the criteria builder
     * @return the created predicate
     */
    protected abstract Predicate createPredicate(@Nullable Root<T> root, @Nullable CriteriaQuery<?> query, @Nullable CriteriaBuilder cb);

    /**
     * Creates a specification that is the AND of this specification and another.
     *
     * @param other the other specification
     * @return the combined specification
     */
    @Override
    @NonNull
    public Specification<T> and(@Nullable Specification<T> other) {
        return Specification.super.and(other);
    }

    /**
     * Creates a specification that is the OR of this specification and another.
     *
     * @param other the other specification
     * @return the combined specification
     */
    @Override
    @NonNull
    public Specification<T> or(@Nullable Specification<T> other) {
        return Specification.super.or(other);
    }

    /**
     * Creates a negated specification.
     *
     * @return the negated specification
     */
    public Specification<T> negate() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.not(createPredicate(root, query, criteriaBuilder));
    }

    /**
     * Utility method to create a predicate for a string like search.
     *
     * @param cb    the criteria builder
     * @param path  the path expression to search in
     * @param value the value to search for
     * @return the created predicate
     */
    protected Predicate like(CriteriaBuilder cb, Expression<String> path, String value) {
        return cb.like(cb.lower(path), "%" + value.toLowerCase() + "%");
    }

    /**
     * Utility class for building complex specifications with multiple conditions.
     */
    public static class Builder<T> {
        private final List<Specification<T>> specifications = new ArrayList<>();

        /**
         * Adds a specification to this builder if a condition is true.
         *
         * @param condition    the condition to check
         * @param specification the specification to add if the condition is true
         * @return this builder
         */
        public Builder<T> with(boolean condition, Specification<T> specification) {
            if (condition) {
                specifications.add(specification);
            }
            return this;
        }

        /**
         * Builds the final specification by combining all added specifications with AND.
         *
         * @return the combined specification
         */
        public Specification<T> build() {
            return specifications.stream()
                    .reduce(Specification.where(null), Specification::and);
        }
    }
}