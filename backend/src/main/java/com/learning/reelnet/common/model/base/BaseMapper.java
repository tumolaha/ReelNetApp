package com.learning.reelnet.common.model.base;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface cơ sở cho mapper giữa Entity và DTO
 * 
 * @param <E> Entity
 * @param <D> DTO
 */
public interface BaseMapper<E, D> {

    /**
     * Chuyển đổi từ Entity sang DTO
     * 
     * @param entity Entity cần chuyển đổi
     * @return DTO tương ứng
     */
    D toDto(E entity);
    
    /**
     * Chuyển đổi từ DTO sang Entity
     * 
     * @param dto DTO cần chuyển đổi
     * @return Entity tương ứng
     */
    E toEntity(D dto);
    
    /**
     * Cập nhật Entity từ DTO
     * 
     * @param dto DTO chứa thông tin cập nhật
     * @param entity Entity cần cập nhật
     * @return Entity đã được cập nhật
     */
    E updateEntityFromDto(D dto, E entity);
    
    /**
     * Chuyển đổi danh sách Entity thành danh sách DTO
     * 
     * @param entities Danh sách Entity
     * @return Danh sách DTO
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
     * Chuyển đổi danh sách DTO thành danh sách Entity
     * 
     * @param dtos Danh sách DTO
     * @return Danh sách Entity
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