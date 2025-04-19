package com.learning.reelnet.common.model.base;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Base interface for mapping between Entity and DTO
 * 
 * @param <E> Entity
 * @param <D> DTO
 */
public interface BaseMapper<E, D> {

    /**
     * Convert from Entity to DTO
     * 
     * @param entity Entity to be converted
     * @return Corresponding DTO
     */
    D toDto(E entity);
    
    /**
     * Convert from DTO to Entity
     * 
     * @param dto DTO to be converted
     * @return Corresponding Entity
     */
    E toEntity(D dto);
    
    /**
     * Update Entity from DTO
     * 
     * @param dto DTO containing update information
     * @param entity Entity to be updated
     * @return Updated entity
     */
    E updateEntityFromDto(D dto, E entity);
    
    /**
     * Convert list of Entities to list of DTOs
     * 
     * @param entities List of Entities
     * @return List of DTOs
     */
    default List<D> toDtoList(List<E> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert list of DTOs to list of Entities
     * 
     * @param dtos List of DTOs
     * @return List of Entities
     */
    default List<E> toEntityList(List<D> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}