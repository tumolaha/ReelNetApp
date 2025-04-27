package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.entity;


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
@Table(name = "antonym")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AntonymEntity extends BaseEntity<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private VocabularyEntity vocabulary;

    @Column(nullable = false)
    private String word;

    private String note;
}