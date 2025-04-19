package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySetItem;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularySetItemRepository;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class JpaVocabularySetItemRepositoryImpl implements VocabularySetItemRepository {
    private final SpringDataVocabularySetItemRepository springDataVocabularySetItemRepository;
    @Override
    public List<VocabularySetItem> saveAll(List<VocabularySetItem> vocabularySetItems) {
        return springDataVocabularySetItemRepository.saveAll(vocabularySetItems);
    }

    @Override
    public void deleteAll(List<VocabularySetItem> vocabularySetItems) {
        springDataVocabularySetItemRepository.deleteAll(vocabularySetItems);
    }

    @Override
    public void deleteAll() {
        springDataVocabularySetItemRepository.deleteAll();
    }

    @Override
    public List<VocabularySetItem> findAllBySetId(UUID setId) {
        return springDataVocabularySetItemRepository.findAllBySetId(setId);
    }

    @Override
    public List<VocabularySetItem> findByVocabularySetId(UUID setId) {
        return springDataVocabularySetItemRepository.findByVocabularySetId(setId);
    }

    @Override
    public List<VocabularySetItem> findAllByVocabularySetId(UUID setId) {
        return springDataVocabularySetItemRepository.findAllByVocabularySetId(setId);
    }
    
    @Override
    public Integer findMaxDisplayOrderBySetId(UUID setId) {
        return springDataVocabularySetItemRepository.findMaxDisplayOrderBySetId(setId);
    }
    
}
