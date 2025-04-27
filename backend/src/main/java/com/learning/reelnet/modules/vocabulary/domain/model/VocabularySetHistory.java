package com.learning.reelnet.modules.vocabulary.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabularySetHistory extends BaseEntity<UUID> {

    private String user;

    private VocabularySet vocabularySet;

    private Integer completedItems;

    private Integer totalItems;

    private Integer correctAnswers;

    private LocalDateTime lastStudied;

    private Double completionPercentage;

    @PrePersist
    protected void onCreate() {
        if (getId() == null) {
            setId(UUID.randomUUID());
        }
        calculateCompletionPercentage();
        lastStudied = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        calculateCompletionPercentage();
        lastStudied = LocalDateTime.now();
    }

    private void calculateCompletionPercentage() {
        if (totalItems != null && totalItems > 0 && completedItems != null) {
            completionPercentage = (double) completedItems / totalItems * 100;
        } else {
            completionPercentage = 0.0;
        }
    }
}