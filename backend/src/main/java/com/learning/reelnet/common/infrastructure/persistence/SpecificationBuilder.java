package com.learning.reelnet.common.infrastructure.persistence;

import com.learning.reelnet.common.application.dto.SearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Builder for creating JPA Specifications from SearchCriteria.
 *
 * @param <T> the entity type
 */
public class SpecificationBuilder<T> {

    private final List<SearchCriteria> criteria;
    private final List<String> searchableFields;

    /**
     * Creates a new SpecificationBuilder.
     */
    public SpecificationBuilder() {
        this.criteria = new ArrayList<>();
        this.searchableFields = new ArrayList<>();
    }

    /**
     * Creates a new SpecificationBuilder with the given criteria.
     *
     * @param criteria the search criteria
     */
    public SpecificationBuilder(List<SearchCriteria> criteria) {
        this.criteria = new ArrayList<>(criteria);
        this.searchableFields = new ArrayList<>();
    }

    /**
     * Set searchable fields for the _all field criterion.
     *
     * @param fields the fields to search in
     * @return this builder
     */
    public SpecificationBuilder<T> withSearchableFields(String... fields) {
        this.searchableFields.clear();
        for (String field : fields) {
            this.searchableFields.add(field);
        }
        return this;
    }

    /**
     * Add a criterion to this builder.
     *
     * @param criterion the criterion to add
     * @return this builder
     */
    public SpecificationBuilder<T> with(SearchCriteria criterion) {
        this.criteria.add(criterion);
        return this;
    }

    /**
     * Add a criterion to this builder if a condition is true.
     *
     * @param condition the condition to check
     * @param criterion the criterion to add if the condition is true
     * @return this builder
     */
    public SpecificationBuilder<T> with(boolean condition, SearchCriteria criterion) {
        if (condition) {
            this.criteria.add(criterion);
        }
        return this;
    }

    /**
     * Build the specification from the collected criteria.
     *
     * @return the built specification
     */
    public Specification<T> build() {
        if (criteria.isEmpty()) {
            return null;
        }

        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (SearchCriteria criterion : criteria) {
                if ("_all".equals(criterion.getField()) && !searchableFields.isEmpty()) {
                    List<Predicate> orPredicates = new ArrayList<>();
                    for (String field : searchableFields) {
                        orPredicates.add(buildPredicate(field, criterion.getOperator(), criterion.getValue(), root, builder));
                    }
                    predicates.add(builder.or(orPredicates.toArray(new Predicate[0])));
                } else {
                    predicates.add(buildPredicate(criterion.getField(), criterion.getOperator(), criterion.getValue(), root, builder));
                }
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Build a predicate for a criterion.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Predicate buildPredicate(String field, SearchCriteria.SearchOperator operator, Object value, Root<T> root, CriteriaBuilder builder) {
        // Handle nested paths (e.g., "user.name")
        Path<?> path = getPath(root, field);
        Class<?> pathType = path.getJavaType();

        // Convert value to proper type
        Object typedValue = convertValue(value, pathType);

        switch (operator) {
            case EQUALS:
                if (typedValue == null) {
                    return builder.isNull(path);
                }
                return builder.equal(path, typedValue);
            case NOT_EQUALS:
                if (typedValue == null) {
                    return builder.isNotNull(path);
                }
                return builder.notEqual(path, typedValue);
            case GREATER_THAN:
                if (pathType.equals(String.class)) {
                    return builder.greaterThan(path.as(String.class), (String) typedValue);
                }
                if (typedValue instanceof Comparable) {
                    return builder.greaterThan(path.as(Comparable.class), (Comparable) typedValue);
                }
                break;
            case LESS_THAN:
                if (pathType.equals(String.class)) {
                    return builder.lessThan(path.as(String.class), (String) typedValue);
                }
                if (typedValue instanceof Comparable) {
                    return builder.lessThan(path.as(Comparable.class), (Comparable) typedValue);
                }
                break;
            case GREATER_EQUAL:
                if (pathType.equals(String.class)) {
                    return builder.greaterThanOrEqualTo(path.as(String.class), (String) typedValue);
                }
                if (typedValue instanceof Comparable) {
                    return builder.greaterThanOrEqualTo(path.as(Comparable.class), (Comparable) typedValue);
                }
                break;
            case LESS_EQUAL:
                if (pathType.equals(String.class)) {
                    return builder.lessThanOrEqualTo(path.as(String.class), (String) typedValue);
                }
                if (typedValue instanceof Comparable) {
                    return builder.lessThanOrEqualTo(path.as(Comparable.class), (Comparable) typedValue);
                }
                break;
            case LIKE:
                if (typedValue instanceof String) {
                    return builder.like(builder.lower(path.as(String.class)), 
                            "%" + ((String) typedValue).toLowerCase() + "%");
                }
                break;
            case STARTS_WITH:
                if (typedValue instanceof String) {
                    return builder.like(builder.lower(path.as(String.class)), 
                            ((String) typedValue).toLowerCase() + "%");
                }
                break;
            case ENDS_WITH:
                if (typedValue instanceof String) {
                    return builder.like(builder.lower(path.as(String.class)), 
                            "%" + ((String) typedValue).toLowerCase());
                }
                break;
            case IN:
                if (typedValue instanceof Collection) {
                    return path.in((Collection<?>) typedValue);
                }
                break;
            case BETWEEN:
                if (typedValue instanceof Object[] && ((Object[]) typedValue).length == 2) {
                    Object[] range = (Object[]) typedValue;
                    if (range[0] instanceof Comparable && range[1] instanceof Comparable) {
                        return builder.between(path.as(Comparable.class), (Comparable) range[0], (Comparable) range[1]);
                    }
                }
                break;
        }

        // Default to equality
        return builder.equal(path, typedValue);
    }

    /**
     * Get a path from a root, handling nested paths.
     *
     * @param root  the root entity
     * @param field the field path, possibly nested (e.g., "user.name")
     * @return the path
     */
    private Path<?> getPath(Root<T> root, String field) {
        String[] parts = field.split("\\.");
        Path<?> path = root;
        for (String part : parts) {
            path = path.get(part);
        }
        return path;
    }

    /**
     * Convert a value to the appropriate type for a path.
     *
     * @param value    the value to convert
     * @param pathType the target type
     * @return the converted value
     */
    private Object convertValue(Object value, Class<?> pathType) {
        if (value == null) {
            return null;
        }

        // Handle simple type conversions
        if (value instanceof String) {
            String stringValue = (String) value;
            
            // Try to convert to appropriate type
            if (pathType.equals(Long.class) || pathType.equals(long.class)) {
                try {
                    return Long.valueOf(stringValue);
                } catch (NumberFormatException e) {
                    return 0L;
                }
            } else if (pathType.equals(Integer.class) || pathType.equals(int.class)) {
                try {
                    return Integer.valueOf(stringValue);
                } catch (NumberFormatException e) {
                    return 0;
                }
            } else if (pathType.equals(Double.class) || pathType.equals(double.class)) {
                try {
                    return Double.valueOf(stringValue);
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            } else if (pathType.equals(Boolean.class) || pathType.equals(boolean.class)) {
                return Boolean.valueOf(stringValue);
            } else if (pathType.equals(LocalDate.class)) {
                try {
                    return LocalDate.parse(stringValue);
                } catch (Exception e) {
                    return LocalDate.now();
                }
            } else if (pathType.equals(LocalDateTime.class)) {
                try {
                    // Try different formats
                    if (stringValue.length() == 10) {
                        return LocalDate.parse(stringValue).atStartOfDay();
                    }
                    return LocalDateTime.parse(stringValue, DateTimeFormatter.ISO_DATE_TIME);
                } catch (Exception e) {
                    return LocalDateTime.now();
                }
            }
        } else if (value instanceof List && ((List<?>) value).size() > 0) {
            // Handle list values for IN operator
            List<?> listValue = (List<?>) value;
            List<Object> convertedList = new ArrayList<>();
            
            for (Object item : listValue) {
                convertedList.add(convertValue(item, pathType));
            }
            
            return convertedList;
        } else if (value instanceof String[] && ((String[]) value).length == 2) {
            // Handle array values for BETWEEN operator
            String[] range = (String[]) value;
            Object[] convertedRange = new Object[2];
            
            convertedRange[0] = convertValue(range[0], pathType);
            convertedRange[1] = convertValue(range[1], pathType);
            
            return convertedRange;
        }

        return value;
    }
}