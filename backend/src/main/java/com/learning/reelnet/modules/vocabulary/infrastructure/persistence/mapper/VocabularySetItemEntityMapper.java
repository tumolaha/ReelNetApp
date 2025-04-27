package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySetItem;
import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.entity.VocabularyEntity;
import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.entity.VocabularySetEntity;
import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.entity.VocabularySetItemEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between VocabularySetItem domain model and VocabularySetItemEntity JPA entity.
 */
@Component
public class VocabularySetItemEntityMapper {

    @Autowired
    private VocabularyEntityMapper vocabularyMapper;
    
    @Autowired
    private VocabularySetEntityMapper vocabularySetMapper;
    
    /**
     * Converts a JPA VocabularySetItemEntity to the VocabularySetItem domain model
     * 
     * @param entity the JPA entity to convert
     * @return the corresponding domain model
     */
    public VocabularySetItem toDomainModel(VocabularySetItemEntity entity) {
        if (entity == null) return null;
        
        VocabularySetItem domainModel = VocabularySetItem.builder()
                .displayOrder(entity.getDisplayOrder())
                .customDefinition(entity.getCustomDefinition())
                .customExample(entity.getCustomExample())
                .notes(entity.getNotes())
                .mastered(entity.isMastered())
                .build();
        
        // Set ID
        domainModel.setId(entity.getId());
        
        // Set relationships if available
        if (entity.getVocabularySet() != null) {
            domainModel.setVocabularySet(vocabularySetMapper.toDomainModel(entity.getVocabularySet()));
        }
        
        if (entity.getVocabulary() != null) {
            domainModel.setVocabulary(vocabularyMapper.toDomainModel(entity.getVocabulary()));
        }
        
        return domainModel;
    }
    
    /**
     * Converts a list of JPA VocabularySetItemEntity objects to a list of VocabularySetItem domain models
     * 
     * @param entities list of JPA entities to convert
     * @return list of corresponding domain models
     */
    public List<VocabularySetItem> toDomainModels(List<VocabularySetItemEntity> entities) {
        if (entities == null) return List.of();
        
        return entities.stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }
    
    /**
     * Converts a VocabularySetItem domain model to a JPA VocabularySetItemEntity
     * 
     * @param domainModel the domain model to convert
     * @param vocabularySetEntity the parent vocabulary set entity (optional)
     * @param vocabularyEntity the parent vocabulary entity (optional)
     * @return the corresponding JPA entity
     */
    public VocabularySetItemEntity toEntity(VocabularySetItem domainModel, 
                                          VocabularySetEntity vocabularySetEntity,
                                          VocabularyEntity vocabularyEntity) {
        if (domainModel == null) return null;
        
        VocabularySetItemEntity entity = VocabularySetItemEntity.builder()
                .displayOrder(domainModel.getDisplayOrder())
                .customDefinition(domainModel.getCustomDefinition())
                .customExample(domainModel.getCustomExample())
                .notes(domainModel.getNotes())
                .mastered(domainModel.isMastered())
                .vocabularySet(vocabularySetEntity)
                .vocabulary(vocabularyEntity)
                .build();
        
        // Set ID if exists
        if (domainModel.getId() != null) {
            entity.setId(domainModel.getId());
        }
        
        return entity;
    }
    
    /**
     * Converts a VocabularySetItem domain model to a JPA VocabularySetItemEntity
     * This overload is used when you don't have the parent entities
     * 
     * @param domainModel the domain model to convert
     * @return the corresponding JPA entity
     */
    public VocabularySetItemEntity toEntity(VocabularySetItem domainModel) {
        if (domainModel == null) return null;
        
        VocabularySetItemEntity entity = VocabularySetItemEntity.builder()
                .displayOrder(domainModel.getDisplayOrder())
                .customDefinition(domainModel.getCustomDefinition())
                .customExample(domainModel.getCustomExample())
                .notes(domainModel.getNotes())
                .mastered(domainModel.isMastered())
                .build();
        
        // Set ID if exists
        if (domainModel.getId() != null) {
            entity.setId(domainModel.getId());
        }
        
        // Set relationships if available
        if (domainModel.getVocabularySet() != null) {
            entity.setVocabularySet(vocabularySetMapper.toEntity(domainModel.getVocabularySet()));
        }
        
        if (domainModel.getVocabulary() != null) {
            entity.setVocabulary(vocabularyMapper.toEntity(domainModel.getVocabulary()));
        }
        
        return entity;
    }
    
    /**
     * Converts a list of VocabularySetItem domain models to a list of JPA VocabularySetItemEntity objects
     * 
     * @param domainModels list of domain models to convert
     * @return list of corresponding JPA entities
     */
    public List<VocabularySetItemEntity> toEntities(List<VocabularySetItem> domainModels) {
        if (domainModels == null) return List.of();
        
        return domainModels.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
