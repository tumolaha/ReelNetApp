package com.learning.reelnet.modules.vocabulary.domain.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.RegisterLabel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sense")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = { "vocabulary", "examples", "registerLabels" })
public class Sense extends BaseEntity<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)

    private Vocabulary vocabulary;

    @Column(nullable = false)
    private String definition; // Định nghĩa bằng tiếng Anh

    @Column(nullable = false)
    private String translation; // Nghĩa tiếng Việt

    @OneToMany(mappedBy = "meaning", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Example> examples; // Danh sách câu ví dụ

    @ElementCollection
    @CollectionTable(name = "sense_register_labels", joinColumns = @JoinColumn(name = "sense_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "register_label")
    private Set<RegisterLabel> registerLabels; // Danh sách phong cách

    private String register;

    private String geographicalUsage;

    private String domain;

    private String grammar;
    
} 