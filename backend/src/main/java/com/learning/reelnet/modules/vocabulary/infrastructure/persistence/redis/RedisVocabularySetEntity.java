package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.Category;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.DifficultyLevel;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.Visibility;

@RedisHash("vocabulary_set")
public class RedisVocabularySetEntity {
    @Id
    private UUID id;
    
    @Indexed
    private String name;
    
    private String description;
    private boolean isActive;
    private String createdBy;
    private Long viewCount;
    private Long likeCount;
    private Long shareCount;
    private Visibility visibility;
    private DifficultyLevel difficultyLevel;
    private Category category;
    private boolean isSystem;
} 