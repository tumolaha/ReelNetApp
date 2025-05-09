package com.learning.reelnet.modules.user.infrastructure.persistence.data;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.reelnet.modules.user.infrastructure.persistence.entity.UserEntity;

/**
 * Spring Data JPA Repository for User entities
 */
@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, String> {
        /**
         * Find a user by their email address
         * 
         * @param email The email address to search for
         * @return An Optional containing the UserEntity if found, or empty if not
         */
        Optional<UserEntity> findByEmail(String email);
        /**
         * Find by last synced date with Auth0 and updated at date
         * 
         * @param date The date to search for users last synced before
         * @return A list of UserEntity found
         */
        List<UserEntity> findByLastSyncTimestampBefore(Date date);
        /**
         * check if a user exists by their ID
         * 
         * @param id The ID to check for existence
         * @return true if the user exists, false otherwise
         */
        boolean existsById(String id);
}
