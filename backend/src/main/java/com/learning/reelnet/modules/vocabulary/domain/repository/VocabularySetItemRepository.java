package com.learning.reelnet.modules.vocabulary.domain.repository;

import java.util.List;
import java.util.UUID;

import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySetItem;

public interface VocabularySetItemRepository {
    /**
     * Save a list of VocabularySetItems to the database.
     *
     * @param vocabularySetItems List of VocabularySetItems to save.
     * @return List of saved VocabularySetItems.
     */
    List<VocabularySetItem> saveAll(List<VocabularySetItem> vocabularySetItems);

    /**
     * Delete a list of VocabularySetItems from the database.
     *
     * @param vocabularySetItems List of VocabularySetItems to delete.
     */
    void deleteAll(List<VocabularySetItem> vocabularySetItems);

    /**
     * Delete all VocabularySetItems from the database.
     */
    void deleteAll();

    /**
     * Find the maximum value of displayOrder by setId.
     *
     * @param setId ID of the VocabularySetItem.
     * @return Maximum value of displayOrder.
     */
    Integer findMaxDisplayOrderBySetId(UUID setId);

    /**
     * Find all VocabularySetItems by setId.
     *
     * @param setId ID of the VocabularySetItem.
     * @return List of VocabularySetItems.
     */
    List<VocabularySetItem> findAllBySetId(UUID setId);

    List<VocabularySetItem> findByVocabularySetId(UUID setId);

    List<VocabularySetItem> findAllByVocabularySetId(UUID setId);
}
