package com.learning.reelnet.modules.vocabulary.domain.model;

import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain model class for Example.
 * This is the pure domain model without JPA annotations.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Example extends BaseEntity<UUID> {

    private Vocabulary vocabulary;
    
    private Sense meaning;
    
    private String sentence; // Câu ví dụ
    
    private String translation; // Dịch câu
    
    private String note;
}