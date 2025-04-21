package com.learning.reelnet.modules.vocabulary.application.mapper;

import com.learning.reelnet.common.model.base.BaseMapper;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet.Visibility;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Mapper cho VocabularySet, chuyển đổi giữa entity và DTO
 */
@Component("vocabularySetMapper")
public class VocabularySetMapper implements BaseMapper<VocabularySet, VocabularySetDto> {

    @Override
    public VocabularySetDto toDto(VocabularySet entity) {
        if (entity == null) {
            return null;
        }
        
        return VocabularySetDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .visibility(entity.getVisibility())
                .difficultyLevel(entity.getDifficultyLevel())
                .category(entity.getCategory())
                .isActive(entity.isActive())
                .createdBy(entity.getCreatedBy())
                .viewCount(entity.getViewCount())
                .likeCount(entity.getLikeCount())
                .shareCount(entity.getShareCount())
                .isSystem(entity.isSystem())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    public VocabularySet toEntity(VocabularySetDto dto) {
        if (dto == null) {
            return null;
        }

        // Tạo instance với các trường bắt buộc
        VocabularySet entity = new VocabularySet(
            dto.getName(),
            dto.getCreatedBy() != null ? dto.getCreatedBy() : "system"
        );
        
        // Set các trường không bắt buộc
        entity.setId(dto.getId() != null ? dto.getId() : UUID.randomUUID());
        entity.setDescription(dto.getDescription());
        entity.setVisibility(dto.getVisibility() != null ? dto.getVisibility() : Visibility.PRIVATE);
        entity.setDifficultyLevel(dto.getDifficultyLevel());
        entity.setCategory(dto.getCategory());
        entity.setActive(dto.isActive());
        entity.setSystem(dto.isSystem());
        entity.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        entity.setUpdatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt() : LocalDateTime.now());
        
        return entity;
    }

    @Override
    public VocabularySet updateEntityFromDto(VocabularySetDto dto, VocabularySet entity) {
        if (dto == null || entity == null) {
            return entity;
        }
        
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        
        if (dto.getVisibility() != null) {
            entity.setVisibility(dto.getVisibility());
        }
        
        if (dto.getDifficultyLevel() != null) {
            entity.setDifficultyLevel(dto.getDifficultyLevel());
        }
        
        if (dto.getCategory() != null) {
            entity.setCategory(dto.getCategory());
        }
        
        // Luôn cập nhật thời gian chỉnh sửa
        entity.setUpdatedAt(LocalDateTime.now());
        
        return entity;
    }

    

}