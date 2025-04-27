package com.learning.reelnet.modules.user.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.learning.reelnet.modules.user.domain.model.User;
import com.learning.reelnet.modules.user.domain.repository.UserRepository;
import com.learning.reelnet.modules.user.infrastructure.persistence.data.SpringDataUserRepository;
import com.learning.reelnet.modules.user.infrastructure.persistence.mapper.UserEntityMapper;

import lombok.RequiredArgsConstructor;

/**
 * JPA implementation of the UserRepository interface.
 * This class bridges between the domain model and the database.
 */
@Repository
@RequiredArgsConstructor
public class JpaUserRepositoryImpl implements UserRepository {
    
    private final SpringDataUserRepository userRepository;
    private final UserEntityMapper userMapper;
    
    @Override
    public Optional<User> findByAuth0Id(String auth0Id) {
        return userRepository.findByAuth0Id(auth0Id)
                .map(userMapper::toDomainModel);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDomainModel);
    }
    
    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toDomainModel);
    }
    
    @Override
    public User save(User user) {
        var entity = userMapper.toEntity(user);
        var savedEntity = userRepository.save(entity);
        return userMapper.toDomainModel(savedEntity);
    }
    
    @Override
    public void delete(User user) {
        var entity = userMapper.toEntity(user);
        userRepository.delete(entity);
    }
    
    @Override
    public boolean existsByAuth0Id(String auth0Id) {
        return userRepository.existsByAuth0Id(auth0Id);
    }
}
