package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.PartOfSpeech;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA Entity for Vocabulary in the database.
 * Maps to the vocabulary table.
 */
@Entity
@Table(name = "vocabulary")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = { "senses", "examples", "synonyms", "antonyms" })
public class VocabularyEntity extends BaseEntity<UUID> {

    @Column(nullable = false, unique = true)
    private String headword;

    @Column(columnDefinition = "TEXT")
    private String meaning;

    @Column(name = "pronunciation_uk")
    private String pronunciationUk;

    @Column(name = "pronunciation_us")
    private String pronunciationUs;

    @Enumerated(EnumType.STRING)
    @Column(name = "pos")
    private PartOfSpeech pos;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "is_system")
    @Builder.Default
    private boolean isSystem = false;

    @Column(name = "view_count")
    @Builder.Default
    private Long viewCount = 0L;

    @Column(name = "difficulty_score")
    private Integer difficultyScore;

    @OneToMany(mappedBy = "vocabulary", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<SenseEntity> senses = new ArrayList<>();

    @OneToMany(mappedBy = "vocabulary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ExampleEntity> examples = new ArrayList<>();

    @OneToMany(mappedBy = "vocabulary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SynonymEntity> synonyms = new HashSet<>();

    @OneToMany(mappedBy = "vocabulary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<AntonymEntity> antonyms = new HashSet<>();

    
}
