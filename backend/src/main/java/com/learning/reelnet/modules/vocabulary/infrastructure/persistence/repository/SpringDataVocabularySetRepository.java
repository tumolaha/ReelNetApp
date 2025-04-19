package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;

/**
 * Spring Data JPA Repository
 * Chỉ chứa các query methods đơn giản
 */
@Repository
public interface SpringDataVocabularySetRepository extends JpaRepository<VocabularySet, UUID>, JpaSpecificationExecutor<VocabularySet> {

        // Tìm kiếm theo nhiều điều kiện, phân trang, sắp xếp, ... sẽ được thực hiện
        @Query("SELECT vs FROM VocabularySet vs WHERE vs.name LIKE %:criteria% OR vs.description LIKE %:criteria%")
        List<VocabularySet> findByCriteria(String criteria); // Method signature only, implementation should be in the

        /*
         * * Tìm kiếm theo ID của người dùng
         */
        @Query("SELECT vs FROM VocabularySet vs WHERE vs.userId = :userId")
        List<VocabularySet> findByUserId(String userId);

        /*
         * * Tìm kiếm theo độ khó
         */
        @Query("SELECT vs FROM VocabularySet vs WHERE vs.visibility = :visibility AND vs.userId = :userId")
        List<VocabularySet> findByVisibilityAndUserId(String visibility, String userId);

        /*
         * * Tìm kiếm theo tên
         */
        @Query("SELECT vs FROM VocabularySet vs WHERE vs.name LIKE %:name%")
        List<VocabularySet> findByCreatedBy(String createdBy);

        /*
         * * Tìm kiếm theo visibility
         * (public, private, friends)
         */
        @Query("SELECT vs FROM VocabularySet vs WHERE vs.difficultyLevel = :difficultyLevel")
        List<VocabularySet> findByVisibility(VocabularySet.Visibility visibility);

        /*
         * * Tìm kiếm theo category
         */
        @Query("SELECT vs FROM VocabularySet vs WHERE vs.difficultyLevel = :difficultyLevel")
        List<VocabularySet> findByCategory(VocabularySet.Category category);

        /*
         * * Tìm kiếm theo độ khó
         */
        @Query("SELECT vs FROM VocabularySet vs WHERE vs.difficultyLevel = :difficultyLevel")
        List<VocabularySet> findByDifficultyLevel(VocabularySet.DifficultyLevel difficultyLevel);

        /*
         * * Tìm kiếm theo tên
         */
        @Query("SELECT vs FROM VocabularySet vs WHERE LOWER(vs.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<VocabularySet> findByNameContainingIgnoreCase(String keyword);

        /*
         * * search
         * Tìm kiếm theo nhiều điều kiện, phân trang, sắp xếp, ... sẽ được thực hiện
         */
        @Query("SELECT vs FROM VocabularySet vs WHERE " +
                        "(:keyword IS NULL OR LOWER(vs.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
                        "(:category IS NULL OR vs.category = :category) AND " +
                        "(:visibility IS NULL OR vs.visibility = :visibility)")
        Page<VocabularySet> search(
                        @Param("keyword") String keyword,
                        @Param("category") VocabularySet.Category category,
                        @Param("visibility") VocabularySet.Visibility visibility,
                        Pageable pageable);

}
