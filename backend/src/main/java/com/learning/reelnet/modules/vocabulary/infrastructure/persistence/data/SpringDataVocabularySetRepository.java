package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.data;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learning.reelnet.common.api.query.utils.QuerySpecificationRepository;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySetHistory;

/**
 * Spring Data JPA Repository
 * Chỉ chứa các query methods đơn giản
 */
@Repository
public interface SpringDataVocabularySetRepository extends QuerySpecificationRepository<VocabularySet, UUID> {

        /*
         * * find vocabulary set of user id
         * 
         * * * @param userId ID of the user whose vocabulary set to find
         * * * @return List of VocabularySet objects belonging to the specified user
         * * * @description This method retrieves all vocabulary sets belonging to a
         */
        @Query("SELECT vs FROM VocabularySet vs WHERE vs.createdBy = :userId")
        Page<VocabularySet> findByUserId(
                @Param("userId") String userId, 
                Pageable pageable);

        /*
         * * Tìm kiếm theo ID của người và recently vocabulary set
         * (có thể có spec)
         */
        @Query("SELECT vh FROM VocabularySetHistory vh " +
                        "WHERE vh.user = :userId " +
                        "AND (:spec is null OR :spec = true)")
        Page<VocabularySetHistory> findRecentlyByUser(
                        @Param("userId") String userId,
                        @Param("spec") Specification<VocabularySet> spec,
                        Pageable pageable);

}
