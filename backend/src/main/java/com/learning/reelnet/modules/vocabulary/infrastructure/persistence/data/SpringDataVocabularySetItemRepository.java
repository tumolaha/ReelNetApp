package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.data;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.entity.VocabularySetItemEntity;

@Repository
public interface SpringDataVocabularySetItemRepository
        extends JpaRepository<VocabularySetItemEntity, UUID>, JpaSpecificationExecutor<VocabularySetItemEntity> {
    
    @Query("SELECT v FROM VocabularySetItemEntity v WHERE v.vocabularySet.id = :setId")
    List<VocabularySetItemEntity> findAllBySetId(UUID setId);

    @Query("SELECT v FROM VocabularySetItemEntity v WHERE v.vocabularySet.id = :setId")
    List<VocabularySetItemEntity> findByVocabularySetId(UUID setId);

    @Query("SELECT v FROM VocabularySetItemEntity v WHERE v.vocabularySet.id = :setId")
    List<VocabularySetItemEntity> findAllByVocabularySetId(UUID setId);

    @Query("SELECT COALESCE(MAX(v.displayOrder), 0) FROM VocabularySetItemEntity v WHERE v.vocabularySet.id = :setId")
    Integer findMaxDisplayOrderBySetId(UUID setId);
}
