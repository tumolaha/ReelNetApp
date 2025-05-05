package com.learning.reelnet.modules.user.infrastructure.persistence.redis;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisUserRepository extends KeyValueRepository<RedisUserEntity, String> {
} 