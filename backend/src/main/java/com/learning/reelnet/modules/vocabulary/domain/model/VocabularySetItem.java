package com.learning.reelnet.modules.vocabulary.domain.model;

import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vocabulary_set_item", uniqueConstraints = @UniqueConstraint(columnNames = { "vocabulary_set_id",
        "vocabulary_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabularySetItem extends BaseEntity<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_set_id", nullable = false)
    private VocabularySet vocabularySet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "custom_definition", length = 1000)
    private String customDefinition;

    @Column(name = "custom_example", length = 1000)
    private String customExample;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "mastered")
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
