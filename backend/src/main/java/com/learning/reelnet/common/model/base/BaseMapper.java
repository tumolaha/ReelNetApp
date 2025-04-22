package com.learning.reelnet.common.model.base;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

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
     * @param dto    DTO containing update information
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

    /*
     * Convert page of Entities to page of DTOs
     * 
     * @param entities Page of Entities
     * 
     * @return Page of DTOs
     */
    default Page<D> toDtoPage(Page<E> entities) {
        if (entities == null) {
            return null;
        }
        return new PageImpl<>(entities.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList()), entities.getPageable(), entities.getTotalElements());
    }

    /**
     * Convert list of DTOs to list of Entities and update existing entities
     * 
     * @param dtos     List of DTOs
     * @param entities List of existing Entities
     * @return List of updated Entities
     */
    default List<E> updateEntityListFromDtoList(List<D> dtos, List<E> entities) {
        if (dtos == null || entities == null) {
            return null;
        }
        for (int i = 0; i < dtos.size(); i++) {
            updateEntityFromDto(dtos.get(i), entities.get(i));
        }
        return entities;
    }
}