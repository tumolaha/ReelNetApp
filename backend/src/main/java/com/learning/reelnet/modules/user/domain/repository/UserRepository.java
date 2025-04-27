package com.learning.reelnet.modules.user.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.learning.reelnet.modules.user.domain.model.User;

/**
 * Repository interface for User domain model.
 * Defines methods for persisting and retrieving User entities.
 */
public interface UserRepository {
    
    /**
     * Find a user by their Auth0 ID
     * 
     * @param auth0Id The Auth0 ID of the user
     * @return An Optional containing the user if found, empty otherwise
     */
    Optional<User> findByAuth0Id(String auth0Id);
    
    /**
     * Find a user by their email address
     * 
     * @param email The user's email address
     * @return An Optional containing the user if found, empty otherwise
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find a user by their UUID
     * 
     * @param id The UUID of the user
     * @return An Optional containing the user if found, empty otherwise
     */
    Optional<User> findById(UUID id);
    
    /**
     * Save a user to the repository
     * 
     * @param user The user to save
     * @return The saved user
     */
    User save(User user);
    
    /**
     * Delete a user from the repository
     * 
     * @param user The user to delete
     */
    void delete(User user);
    
    /**
     * Check if a user with the given Auth0 ID exists
     * 
     * @param auth0Id The Auth0 ID to check
     * @return True if a user with the given Auth0 ID exists, false otherwise
     */
    boolean existsByAuth0Id(String auth0Id);
}
