package com.learning.reelnet.modules.vocabulary.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;
import com.learning.reelnet.modules.user.domain.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "vocabulary_set_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabularySetHistory extends BaseEntity<UUID> {

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "vocabulary_set_id", nullable = false)
    private VocabularySet vocabularySet;

    @Column(name = "completed_items")
    private Integer completedItems;

    @Column(name = "total_items")
    private Integer totalItems;

    @Column(name = "correct_answers")
    private Integer correctAnswers;

    @Column(name = "last_studied")
    private LocalDateTime lastStudied;

    @Column(name = "completion_percentage")
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