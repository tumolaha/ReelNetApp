package com.learning.reelnet.modules.user.infrastructure.persistence.data;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.reelnet.modules.user.infrastructure.persistence.entity.RoleEntity;

@Repository
public interface SpringDataRoleRepository extends JpaRepository<RoleEntity, UUID> {
    
    Optional<RoleEntity> findByName(String name);
    
    Optional<RoleEntity> findByAuth0RoleId(String auth0RoleId);
    
    List<RoleEntity> findByNameIn(Set<String> names);
}
