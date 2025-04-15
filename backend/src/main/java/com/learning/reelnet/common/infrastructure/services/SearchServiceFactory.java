package com.learning.reelnet.common.infrastructure.services;

import com.learning.reelnet.common.domain.base.BaseEntity;
import com.learning.reelnet.common.domain.base.BaseRepository;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating and caching search services for specific entity types.
 * This helps avoid creating multiple instances of the same service.
 */
@Component
public class SearchServiceFactory {

    private final Map<Class<?>, SearchService<?, ?, ?>> serviceCache = new HashMap<>();

    /**
     * Get or create a search service for the given entity class and repository.
     * If a service for this entity type already exists, it will be returned.
     * Otherwise, a new service will be created and cached.
     *
     * @param entityClass the entity class
     * @param repository  the repository for the entity
     * @param <T>         the entity type
     * @param <ID>        the entity ID type
     * @param <R>         the repository type
     * @return a search service for the entity type
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity<ID>, ID extends Serializable, R extends BaseRepository<T, ID>> SearchService<T, ID, R> getService(
            Class<T> entityClass, R repository) {
        
        return (SearchService<T, ID, R>) serviceCache.computeIfAbsent(
                entityClass,
                clazz -> new GenericSearchService<>(repository)
        );
    }

    /**
     * Get or create a search service for the given entity class and repository,
     * with the specified searchable fields.
     *
     * @param entityClass      the entity class
     * @param repository       the repository for the entity
     * @param searchableFields the fields to search in for the _all criterion
     * @param <T>              the entity type
     * @param <ID>             the entity ID type
     * @param <R>              the repository type
     * @return a search service for the entity type
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity<ID>, ID extends Serializable, R extends BaseRepository<T, ID>> SearchService<T, ID, R> getService(
            Class<T> entityClass, R repository, String... searchableFields) {
        
        SearchService<T, ID, R> service = (SearchService<T, ID, R>) serviceCache.computeIfAbsent(
                entityClass,
                clazz -> new GenericSearchService<>(repository)
        );
        
        return service.withSearchableFields(searchableFields);
    }
} 