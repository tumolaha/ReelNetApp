package com.learning.reelnet.modules.vocabulary.domain.model; 


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "example")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Example extends BaseEntity<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sense_id")
    private Sense meaning; // This is the missing field referenced by Sense.examples

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sentence; // Câu ví dụ
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String translation; // Dịch câu

    @Column(nullable = false, columnDefinition = "TEXT")
    private String note;
} 