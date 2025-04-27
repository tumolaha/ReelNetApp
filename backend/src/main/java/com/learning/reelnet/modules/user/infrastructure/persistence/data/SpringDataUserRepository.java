package com.learning.reelnet.modules.user.infrastructure.persistence.data;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.reelnet.modules.user.infrastructure.persistence.entity.UserEntity;


/**
 * Spring Data JPA Repository for User entities
 */
@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {
    
    /**
     * Find a user by their Auth0 ID
     * 
     * @param auth0Id The Auth0 ID of the user
     * @return An Optional containing the user if found, empty otherwise
     */
    Optional<UserEntity> findByAuth0Id(String auth0Id);
    
    /**
     * Find a user by their email address
     * 
     * @param email The user's email address
     * @return An Optional containing the user if found, empty otherwise
     */
    Optional<UserEntity> findByEmail(String email);
    
    /**
     * Check if a user with the given Auth0 ID exists
     * 
     * @param auth0Id The Auth0 ID to check
     * @return True if a user with the given Auth0 ID exists, false otherwise
     */
    boolean existsByAuth0Id(String auth0Id);
}
