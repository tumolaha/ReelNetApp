package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;
import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.entity.VocabularySetEntity;

@Component
public class VocabularySetEntityMapper {
    public VocabularySetEntity toEntity(VocabularySet vocabularySet) {
        if (vocabularySet == null) return null;
        
        VocabularySetEntity entity = VocabularySetEntity.builder()
            .name(vocabularySet.getName())
            .description(vocabularySet.getDescription())
            .build();
        
        entity.setId(vocabularySet.getId());
        entity.setCreatedAt(vocabularySet.getCreatedAt());
        entity.setUpdatedAt(vocabularySet.getUpdatedAt());

        return entity;
    }
    public VocabularySet toDomainModel(VocabularySetEntity entity) {
        if (entity == null) return null;
        
        VocabularySet vocabularySet = VocabularySet.builder()
            .name(entity.getName())
            .description(entity.getDescription())
            .build();
        vocabularySet.setId(entity.getId());
        vocabularySet.setCreatedAt(entity.getCreatedAt());
        vocabularySet.setUpdatedAt(entity.getUpdatedAt());
        return vocabularySet;   
        
    }

    public List<VocabularySet> toDomainModels(List<VocabularySetEntity> entities) {
        return entities.stream()
            .map(this::toDomainModel)
            .toList();
    }
    
    public List<VocabularySetEntity> toEntities(List<VocabularySet> vocabularySets) {
        return vocabularySets.stream()
            .map(this::toEntity)
            .toList();
    }
}
