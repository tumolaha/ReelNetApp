package com.learning.reelnet.modules.vocabulary.domain.model;



import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Metadata extends BaseEntity<UUID> {

    @ManyToOne
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;

    @Column
    private String frequency; // Mức độ phổ biến (A1, B2, C1, etc.)

    @Column
    private String region; // Vùng miền sử dụng (US, UK, AU...)

    @Column
    private String domain; // Chuyên ngành (IT, medical, legal...)
} 