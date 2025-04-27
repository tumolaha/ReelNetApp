package com.learning.reelnet.modules.user.infrastructure.persistence.repository;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.learning.reelnet.modules.user.domain.model.UserPermission;
import com.learning.reelnet.modules.user.domain.repository.UserPermissionRepository;
import com.learning.reelnet.modules.user.infrastructure.persistence.data.SpringDataUserPermissionRepository;
import com.learning.reelnet.modules.user.infrastructure.persistence.mapper.UserPermissionEntityMapper;

import lombok.RequiredArgsConstructor;

/**
 * JPA implementation of UserPermissionRepository.
 */
@Repository
@RequiredArgsConstructor
public class JpaUserPermissionRepositoryImpl implements UserPermissionRepository {

    private final SpringDataUserPermissionRepository permissionRepository;
    private final UserPermissionEntityMapper permissionMapper;    @Override
    public Set<UserPermission> findByUserId(UUID userId) {
        return permissionRepository.findByUser_Id(userId).stream()
                .map(permissionMapper::toDomainModel)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<UserPermission> findByUser_Id(UUID userId) {
        // This is a duplicate method but kept for API compatibility
        return findByUserId(userId);
    }

    @Override
    public UserPermission save(UserPermission userPermission) {
        var entity = permissionMapper.toEntity(userPermission);
        var savedEntity = permissionRepository.save(entity);
        return permissionMapper.toDomainModel(savedEntity);
    }

    @Override
    public void delete(UserPermission userPermission) {
        var entity = permissionMapper.toEntity(userPermission);
        permissionRepository.delete(entity);
    }
}
