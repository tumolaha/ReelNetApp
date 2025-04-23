package com.learning.reelnet.modules.user.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.learning.reelnet.modules.user.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    /**
     * Find a user by their Auth0 ID
     * @param auth0Id the Auth0 ID
     * @return the user if found
     */
    Optional<User> findByAuth0Id(String auth0Id);
    
    /**
     * Find a user by their email
     * @param email the email
     * @return the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find all creators (pageable)
     * @param pageable pagination information
     * @return page of creators
     */
    Page<User> findByCreatorTrue(Pageable pageable);
    
    /**
     * Find users by role
     * @param role the role to search for
     * @param pageable pagination information
     * @return page of users with the given role
     */
    Page<User> findByRole(String role, Pageable pageable);
    
    /**
     * Search users by email or display name containing the search term
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return page of matching users
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(u.displayName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> searchUsers(String searchTerm, Pageable pageable);
    
    /**
     * Get recently active users
     * @param limit max number of users to return
     * @return list of recently active users
     */
    @Query(value = "SELECT * FROM users ORDER BY last_login DESC NULLS LAST LIMIT :limit", nativeQuery = true)
    List<User> findRecentlyActiveUsers(int limit);
} 