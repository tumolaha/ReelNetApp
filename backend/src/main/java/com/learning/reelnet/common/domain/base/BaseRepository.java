package com.learning.reelnet.common.domain.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.io.Serializable;

/**
 * Base repository interface with common functionality for all repositories.
 *
 * @param <T>  the entity type
 * @param <ID> the type of the entity ID
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity<ID>, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * Finds an entity by ID and locks it for update.
     *
     * @param id the entity ID
     * @return the entity, or null if not found
     */
    T findByIdAndLock(ID id);

    /**
     * Checks if an entity with the given ID exists.
     *
     * @param id the entity ID
     * @return true if the entity exists, false otherwise
     */
    default boolean existsById(@NonNull ID id) {
        return findById(id).isPresent();
    }

}