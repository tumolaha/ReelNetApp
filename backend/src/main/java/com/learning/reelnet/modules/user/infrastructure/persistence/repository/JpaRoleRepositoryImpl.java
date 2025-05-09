package com.learning.reelnet.modules.user.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.learning.reelnet.modules.user.domain.model.Role;
import com.learning.reelnet.modules.user.domain.repository.RoleRepository;
import com.learning.reelnet.modules.user.infrastructure.persistence.data.SpringDataRoleRepository;
import com.learning.reelnet.modules.user.infrastructure.persistence.mapper.RoleEntityMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaRoleRepositoryImpl implements RoleRepository {

    private final SpringDataRoleRepository springDataRoleRepository;
    private final RoleEntityMapper roleMapper;
    
    @Override
    public Optional<Role> findById(UUID id) {
        return springDataRoleRepository.findById(id)
                .map(roleMapper::toDomain);
    }
    
    @Override
    public Optional<Role> findByName(String name) {
        return springDataRoleRepository.findByName(name)
                .map(roleMapper::toDomain);
    }
    
    @Override
    public Optional<Role> findByAuth0RoleId(String auth0RoleId) {
        return springDataRoleRepository.findByAuth0RoleId(auth0RoleId)
                .map(roleMapper::toDomain);
    }
    
    @Override
    public Role save(Role role) {
        return roleMapper.toDomain(
            springDataRoleRepository.save(roleMapper.toEntity(role))
        );
    }
    
    @Override
    public List<Role> findAll() {
        return springDataRoleRepository.findAll().stream()
                .map(roleMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Role> findAllByNameIn(Set<String> names) {
        return springDataRoleRepository.findByNameIn(names).stream()
                .map(roleMapper::toDomain)
                .collect(Collectors.toList());
    }
}
