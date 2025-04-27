package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySetItem;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularySetItemRepository;
import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.data.SpringDataVocabularySetItemRepository;

import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.mapper.VocabularySetItemEntityMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaVocabularySetItemRepositoryImpl implements VocabularySetItemRepository {
    private final SpringDataVocabularySetItemRepository springDataRepository;
    private final VocabularySetItemEntityMapper mapper;
    
    @Override
    public List<VocabularySetItem> saveAll(List<VocabularySetItem> vocabularySetItems) {
        var entities = mapper.toEntities(vocabularySetItems);
        var savedEntities = springDataRepository.saveAll(entities);
        return mapper.toDomainModels(savedEntities);
    }

    @Override
    public void deleteAll(List<VocabularySetItem> vocabularySetItems) {
        var entities = mapper.toEntities(vocabularySetItems);
        springDataRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        springDataRepository.deleteAll();
    }

    @Override
    public List<VocabularySetItem> findAllBySetId(UUID setId) {
        var entities = springDataRepository.findAllBySetId(setId);
        return mapper.toDomainModels(entities);
    }

    @Override
    public List<VocabularySetItem> findByVocabularySetId(UUID setId) {
        var entities = springDataRepository.findByVocabularySetId(setId);
        return mapper.toDomainModels(entities);
    }

    @Override
    public List<VocabularySetItem> findAllByVocabularySetId(UUID setId) {
        var entities = springDataRepository.findAllByVocabularySetId(setId);
        return mapper.toDomainModels(entities);
    }
    
    @Override
    public Integer findMaxDisplayOrderBySetId(UUID setId) {
        return springDataRepository.findMaxDisplayOrderBySetId(setId);
    }
}
