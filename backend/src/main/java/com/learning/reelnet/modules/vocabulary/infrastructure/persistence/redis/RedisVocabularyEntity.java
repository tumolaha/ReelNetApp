package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.PartOfSpeech;

@RedisHash("vocabulary")
public class RedisVocabularyEntity {
    @Id
    private UUID id;
    
    @Indexed
    private String headword;
    
    private String meaning;
    private String pronunciationUk;
    private String pronunciationUs;
    private PartOfSpeech pos;
    private String createdBy;
    private boolean isSystem;
    private Long viewCount;
    private Integer difficultyScore;
} 