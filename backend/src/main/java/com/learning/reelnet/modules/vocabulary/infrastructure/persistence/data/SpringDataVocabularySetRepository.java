package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.data;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learning.reelnet.common.api.query.utils.QuerySpecificationRepository;
import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.entity.VocabularySetEntity;
@Repository
public interface SpringDataVocabularySetRepository extends QuerySpecificationRepository<VocabularySetEntity, UUID> {

    @Query("SELECT vs FROM VocabularySetEntity vs " +
           "WHERE vs.createdBy = :userId " +
           "AND (:spec IS NULL OR :spec = true)")
    Page<VocabularySetEntity> findRecentlyByUser(
            @Param("userId") String userId,
            @Param("spec") Specification<VocabularySetEntity> spec,
            Pageable pageable);

    @Query("SELECT vs FROM VocabularySetEntity vs WHERE vs.createdBy = :userId")
    Page<VocabularySetEntity> findByUserId(
            @Param("userId") String userId, 
            Pageable pageable);
}
