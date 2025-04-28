package com.learning.reelnet.modules.user.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.learning.reelnet.modules.user.domain.model.User;
import com.learning.reelnet.modules.user.domain.repository.UserRepository;
import com.learning.reelnet.modules.user.infrastructure.persistence.data.SpringDataUserRepository;
import com.learning.reelnet.modules.user.infrastructure.persistence.entity.UserEntity;
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
    public Optional<User> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email).map(userMapper::toDomain);
        if (user.isPresent()) {
            User userEntity = user.get();
            userEntity.setLastLogin(LocalDateTime.now());
            userRepository.save(userMapper.toEntity(userEntity));
        }
        return user;
    }

    @Override
    public List<User> findByLastSyncedWithAuth0Before(Date date) {
        List<User> users = userRepository.findByLastSyncedWithAuth0Before(date).stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
        for (User user : users) {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(userMapper.toEntity(user));
        }
        return users;
    }

    @Override
    public boolean existsById(String id) {
        return userRepository.existsById(id);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        return userMapper.toDomain(userRepository.save(userEntity));
    }

}
