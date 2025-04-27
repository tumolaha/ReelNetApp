package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import org.springframework.stereotype.Component;

import com.learning.reelnet.modules.vocabulary.domain.model.Vocabulary;
import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.entity.VocabularyEntity;

/**
 * Mapper class for converting between Vocabulary domain model and VocabularyEntity JPA entity.
 */
@Component
public class VocabularyEntityMapper {

    /**
     * Converts a JPA VocabularyEntity to the Vocabulary domain model
     * 
     * @param entity the JPA entity to convert
     * @return the corresponding domain model
     */
    public Vocabulary toDomainModel(VocabularyEntity entity) {
        if (entity == null) return null;
        
        // Create basic vocabulary
        Vocabulary vocabulary = Vocabulary.builder()
                .headword(entity.getHeadword())
                .meaning(entity.getMeaning())
                .pronunciationUk(entity.getPronunciationUk())
                .pronunciationUs(entity.getPronunciationUs())
                .pos(entity.getPos())
                .createdBy(entity.getCreatedBy())
                .isSystem(entity.isSystem())
                .viewCount(entity.getViewCount())
                .difficultyScore(entity.getDifficultyScore())
                .build();
        
        // Set ID and audit fields
        vocabulary.setId(entity.getId());
        vocabulary.setCreatedAt(entity.getCreatedAt());
        vocabulary.setUpdatedAt(entity.getUpdatedAt());
        vocabulary.setCreatedBy(entity.getCreatedBy());
        vocabulary.setUpdatedBy(entity.getUpdatedBy());
        
        // Map related entities
        // For now, we're creating empty collections that will need to be properly mapped
        // In a complete implementation, you would map each related entity
        vocabulary.setSenses(new ArrayList<>());
        vocabulary.setExamples(new ArrayList<>());
        vocabulary.setSynonyms(new HashSet<>());
        vocabulary.setAntonyms(new HashSet<>());
        
        return vocabulary;
    }
    
    /**
     * Converts a Vocabulary domain model to a JPA VocabularyEntity
     * 
     * @param vocabulary the domain model to convert
     * @return the corresponding JPA entity
     */
    public VocabularyEntity toEntity(Vocabulary vocabulary) {
        if (vocabulary == null) return null;
        
        // Create basic vocabulary entity
        VocabularyEntity entity = VocabularyEntity.builder()
                .headword(vocabulary.getHeadword())
                .meaning(vocabulary.getMeaning())
                .pronunciationUk(vocabulary.getPronunciationUk())
                .pronunciationUs(vocabulary.getPronunciationUs())
                .pos(vocabulary.getPos())
                .createdBy(vocabulary.getCreatedBy())
                .isSystem(vocabulary.isSystem())
                .viewCount(vocabulary.getViewCount())
                .difficultyScore(vocabulary.getDifficultyScore())
                .build();
        
        // Set ID and audit fields
        entity.setId(vocabulary.getId());
        entity.setCreatedAt(vocabulary.getCreatedAt());
        entity.setUpdatedAt(vocabulary.getUpdatedAt());
        entity.setCreatedBy(vocabulary.getCreatedBy());
        entity.setUpdatedBy(vocabulary.getUpdatedBy());
        
        // Map related entities
        // For now, we're creating empty collections that will need to be properly mapped
        // In a complete implementation, you would map each related entity
        entity.setSenses(new ArrayList<>());
        entity.setExamples(new ArrayList<>());
        entity.setSynonyms(new HashSet<>());
        entity.setAntonyms(new HashSet<>());
        
        return entity;
    }
    
    
    
    // Similar methods for Example, Synonym, Antonym
}
