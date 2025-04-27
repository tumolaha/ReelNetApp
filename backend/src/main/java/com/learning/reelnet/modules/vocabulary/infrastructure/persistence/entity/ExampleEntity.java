package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.entity;

import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA Entity for Example in the database.
 * Maps to the example table.
 */
@Entity
@Table(name = "example")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExampleEntity extends BaseEntity<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private VocabularyEntity vocabulary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sense_id")
    private SenseEntity meaning; // Liên kết với sense

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sentence; // Câu ví dụ
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String translation; // Dịch câu

    @Column(nullable = false, columnDefinition = "TEXT")
    private String note;
}
