package com.learning.reelnet.modules.user.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Set;

import com.learning.reelnet.modules.user.domain.model.Role;

public interface RoleRepository {
    
    Optional<Role> findById(UUID id);
    
    Optional<Role> findByName(String name);
    
    Optional<Role> findByAuth0RoleId(String auth0RoleId);
    
    Role save(Role role);
    
    List<Role> findAll();
    
    List<Role> findAllByNameIn(Set<String> names);
}
