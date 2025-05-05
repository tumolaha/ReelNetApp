package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.redis;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RedisVocabularySetItemRepository extends KeyValueRepository<RedisVocabularySetItemEntity, UUID> {
} 