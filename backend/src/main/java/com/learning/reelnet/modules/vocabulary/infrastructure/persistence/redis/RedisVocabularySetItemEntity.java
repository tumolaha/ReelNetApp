package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@RedisHash("vocabulary_set_item")
public class RedisVocabularySetItemEntity {
    @Id
    private UUID id;
    
    @Indexed
    private UUID vocabularySetId;
    
    @Indexed
    private UUID vocabularyId;
    
    private Integer displayOrder;
    private String customDefinition;
    private String customExample;
    private String notes;
    private boolean mastered;
} 