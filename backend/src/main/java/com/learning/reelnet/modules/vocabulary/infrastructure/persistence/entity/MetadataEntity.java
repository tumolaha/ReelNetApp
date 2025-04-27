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
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "example")
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataEntity extends BaseEntity<UUID> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private VocabularyEntity vocabulary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sense_id")
    private SenseEntity meaning;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sentence; // Câu ví dụ
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String translation; // Dịch câu

    @Column(nullable = false, columnDefinition = "TEXT")
    private String note;
}
