package com.learning.reelnet.modules.vocabulary.domain.model;

import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain model for VocabularySetItem representing an item in a vocabulary set.
 * This is a pure domain model without JPA annotations.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabularySetItem extends BaseEntity<UUID> {

    private VocabularySet vocabularySet;
    
    private Vocabulary vocabulary;
    
    private Integer displayOrder;
    
    private String customDefinition;
    
    private String customExample;
    
    private String notes;
    
    @Builder.Default
    private boolean mastered = false;

    /**
     * Updates custom definition with validation
     */
    public void updateCustomDefinition(String definition) {
        if (definition != null && definition.length() > 1000) {
            this.customDefinition = definition.substring(0, 1000);
        } else {
            this.customDefinition = definition;
        }
    }

    /**
     * Updates custom example with validation
     */
    public void updateCustomExample(String example) {
        if (example != null && example.length() > 1000) {
            this.customExample = example.substring(0, 1000);
        } else {
            this.customExample = example;
        }
    }

    /**
     * Updates notes with validation
     */
    public void updateNotes(String notes) {
        if (notes != null && notes.length() > 500) {
            this.notes = notes.substring(0, 500);
        } else {
            this.notes = notes;
        }
    }

    /**
     * Toggle mastered status
     */
    public void toggleMastered() {
        this.mastered = !this.mastered;
    }
}
