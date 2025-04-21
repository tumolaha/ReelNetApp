package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySetItem;


@Repository
public interface SpringDataVocabularySetItemRepository
        extends JpaRepository<VocabularySetItem, UUID>, JpaSpecificationExecutor<VocabularySetItem> {
    /*
     * * * Find all VocabularySetItem by their set ID.
     * * * @param setId UUID representing the ID of the VocabularySet to be found.
     */
    @Query("SELECT MAX(v.displayOrder) FROM VocabularySetItem v WHERE v.vocabularySet.id = ?1")
    public List<VocabularySetItem> findAllBySetId(UUID setId);

    /*
     * * * Find all VocabularySetItem by their set ID.
     * * * @param setId UUID representing the ID of the VocabularySet to be found.
     */
    @Query("SELECT v FROM VocabularySetItem v WHERE v.vocabularySet.id = ?1")   
    public List<VocabularySetItem> findByVocabularySetId(UUID setId);
    /*
     * * * Find all VocabularySetItem by their set ID.
     * * * @param setId UUID representing the ID of the VocabularySet to be found.
     */
    @Query("SELECT v FROM VocabularySetItem v WHERE v.vocabularySet.id = ?1")        
    public List<VocabularySetItem> findAllByVocabularySetId(UUID setId);

    /*
     * * * Find the maximum display order for VocabularySetItem by their set ID.
     * * * @param setId UUID representing the ID of the VocabularySet to be found.
     */
    @Query("SELECT MAX(v.displayOrder) FROM VocabularySetItem v WHERE v.vocabularySet.id = ?1")
    public Integer findMaxDisplayOrderBySetId(UUID setId); // Tìm max display order của vocabulary set item trong
                                                           // vocabulary set

}
