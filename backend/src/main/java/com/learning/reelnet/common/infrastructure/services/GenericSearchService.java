package com.learning.reelnet.common.infrastructure.services;

import com.learning.reelnet.common.domain.base.BaseEntity;
import com.learning.reelnet.common.domain.base.BaseRepository;

import java.io.Serializable;

/**
 * Generic implementation of SearchService that can be used for any entity.
 * This class is not meant to be used directly, but rather to be subclassed
 * for specific entity types.
 *
 * @param <T>  the entity type
 * @param <ID> the entity ID type
 * @param <R>  the repository type
 */
public class GenericSearchService<T extends BaseEntity<ID>, ID extends Serializable, R extends BaseRepository<T, ID>> 
        extends SearchService<T, ID, R> {

    /**
     * Creates a new GenericSearchService with the given repository.
     *
     * @param repository the repository to use
     */
    public GenericSearchService(R repository) {
        super(repository);
    }
} 