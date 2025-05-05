package com.learning.reelnet.common.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(
    basePackages = "com.learning.reelnet.modules.*.infrastructure.persistence.redis",
    repositoryImplementationPostfix = "Impl"
)
public class RedisRepositoryConfig {
} 