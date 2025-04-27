package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "etymology")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EtymologyEntity extends BaseEntity<UUID> {

    @ManyToOne
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private VocabularyEntity vocabulary;

    @Column(nullable = false)
    private String originLanguage; // Ngôn ngữ gốc (Latin, Greek, etc.)

    @Column(nullable = false)
    private String etymologyDescription;// Mô tả nguồn gốc từ
} 