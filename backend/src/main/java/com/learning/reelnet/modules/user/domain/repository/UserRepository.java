package com.learning.reelnet.modules.user.domain.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.learning.reelnet.modules.user.domain.model.User;

/**
 * Repository interface for User domain model.
 * Defines methods for persisting and retrieving User entities.
 */
public interface UserRepository {
    
    Optional<User> findByEmail(String email);
    List<User> findByLastSyncedWithAuth0Before(Date date);
    boolean existsById(String id);


    Optional<User> findById(String id);
    User save(User user);
}
