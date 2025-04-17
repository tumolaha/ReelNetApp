package com.learning.reelnet.modules.vocabulary.domain.model;

import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "synonym")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Synonym extends BaseEntity<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;

    @Column(nullable = false)
    private String word;

    private String note;
}