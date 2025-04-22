package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.learning.reelnet.common.api.query.utils.QuerySpecificationRepository;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;

/**
 * Spring Data JPA Repository
 * Chỉ chứa các query methods đơn giản
 */
@Repository
public interface SpringDataVocabularySetRepository extends QuerySpecificationRepository<VocabularySet, UUID> {

        // Tìm kiếm theo nhiều điều kiện, phân trang, sắp xếp, ... sẽ được thực hiện
        @Query("SELECT vs FROM VocabularySet vs WHERE vs.name LIKE %:criteria% OR vs.description LIKE %:criteria%")
        List<VocabularySet> findByCriteria(String criteria);

        /*
         * * Tìm kiếm theo ID của người tạo
         */
        @Query("SELECT vs FROM VocabularySet vs WHERE vs.createdBy = :userId")
        List<VocabularySet> findByUserId(String userId);

        /*
         * * Tìm kiếm theo visibility và người tạo
         */
        @Query("SELECT vs FROM VocabularySet vs WHERE vs.visibility = :visibility AND vs.createdBy = :userId")
        List<VocabularySet> findByVisibilityAndUserId(String visibility, String userId);

        /*
         * * Tìm kiếm theo người tạo
         */
        @Query("SELECT vs FROM VocabularySet vs WHERE vs.createdBy LIKE %:createdBy%")
        List<VocabularySet> findByCreatedBy(String createdBy);

        /*
         * * Tìm kiếm theo visibility
         * (public, private, friends)
         */
        @Query("SELECT vs FROM VocabularySet vs WHERE vs.visibility = :visibility")
        List<VocabularySet> findByVisibility(VocabularySet.Visibility visibility);

        /*
         * * Tìm kiếm theo category
         */
        @Query("SELECT vs FROM VocabularySet vs WHERE vs.category = :category")
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

        /*
         * Xóa tất cả vocabulary set của một user
         */
        @Modifying
        @Transactional
        @Query("DELETE FROM VocabularySet vs WHERE vs.createdBy = :userId")
        void deleteAllByUserId(String userId);

        /*
         * Xóa vocabulary set theo category
         */
        @Modifying
        @Transactional
        @Query("DELETE FROM VocabularySet vs WHERE vs.category = :category")
        void deleteByCategory(VocabularySet.Category category);

        /*
         * Xóa vocabulary set không active trong một thời gian
         */
        @Modifying
        @Transactional
        @Query("DELETE FROM VocabularySet vs WHERE vs.isActive = false AND vs.updatedAt < :date")
        void deleteInactiveOlderThan(@Param("date") java.time.LocalDateTime date);

        /*
         * Soft delete - cập nhật trạng thái isActive thành false
         */
        @Modifying
        @Transactional
        @Query("UPDATE VocabularySet vs SET vs.isActive = false WHERE vs.id = :id")
        void softDelete(@Param("id") UUID id);

        /*
         * Khôi phục vocabulary set đã soft delete
         */
        @Modifying
        @Transactional
        @Query("UPDATE VocabularySet vs SET vs.isActive = true WHERE vs.id = :id")
        void restore(@Param("id") UUID id);
}
