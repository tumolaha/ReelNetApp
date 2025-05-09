package com.learning.reelnet.modules.user.infrastructure.persistence.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.learning.reelnet.modules.user.api.dto.UserSearchCriteria;
import com.learning.reelnet.modules.user.domain.model.User;
import com.learning.reelnet.modules.user.domain.repository.UserRepository;
import com.learning.reelnet.modules.user.infrastructure.persistence.data.SpringDataUserRepository;
import com.learning.reelnet.modules.user.infrastructure.persistence.entity.UserEntity;
import com.learning.reelnet.modules.user.infrastructure.persistence.mapper.UserEntityMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

/**
 * JPA implementation of the UserRepository interface.
 * This class bridges between the domain model and the database.
 */
@Repository
@RequiredArgsConstructor
public class JpaUserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private final SpringDataUserRepository userRepository;
    private final UserEntityMapper userMapper;

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email).map(userMapper::toDomain);
        if (user.isPresent()) {
            User userEntity = user.get();
            userEntity.setLastLogin(Instant.now());
            userRepository.save(userMapper.toEntity(userEntity));
        }
        return user;
    }

    @Override
    public List<User> findByLastSyncTimestampBefore(Date date) {
        List<User> users = userRepository.findByLastSyncTimestampBefore(date).stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
        for (User user : users) {
            user.setLastLogin(Instant.now());
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

    @Override
    public Page<User> findByCriteria(UserSearchCriteria criteria, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        // Thêm điều kiện tìm kiếm dựa trên criteria
        if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("email")),
                    "%" + criteria.getEmail().toLowerCase() + "%"));
        }

        if (criteria.getName() != null && !criteria.getName().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                    "%" + criteria.getName().toLowerCase() + "%"));
        }

        if (criteria.getActive() != null) {
            predicates.add(cb.equal(root.get("active"), criteria.getActive()));
        }

        if (criteria.getEmailVerified() != null) {
            predicates.add(cb.equal(root.get("emailVerified"), criteria.getEmailVerified()));
        }

        if (criteria.getTenantId() != null) {
            predicates.add(cb.equal(root.get("tenantId"), criteria.getTenantId()));
        }

        if (criteria.getLastLoginAfter() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("lastLogin"), criteria.getLastLoginAfter()));
        }

        if (criteria.getLastLoginBefore() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("lastLogin"), criteria.getLastLoginBefore()));
        }

        // Thêm điều kiện cho roleId (cần join với bảng role nếu cần)
        if (criteria.getRoleId() != null) {
            predicates.add(cb.isMember(criteria.getRoleId(), root.get("roles")));
        }

        // Kết hợp tất cả predicates với AND
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        // Thêm sắp xếp
        if (pageable.getSort().isSorted()) {
            query.orderBy(/* add order by based on pageable */);
        }

        // Thực hiện query để lấy tổng số bản ghi
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);
        countQuery.select(cb.count(countRoot))
                .where(cb.and(predicates.toArray(new Predicate[0])));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        // Thực hiện query chính với phân trang
        List<User> users = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public Page<User> findByEmailContainingIgnoreCase(String emailPart, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmailContainingIgnoreCase'");
    }

}
