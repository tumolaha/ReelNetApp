package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.learning.reelnet.modules.vocabulary.domain.model.Vocabulary;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularyRepository;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.PartOfSpeech;
import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.data.SpringDataVocabularyRepository;

import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.mapper.VocabularyEntityMapper;

import lombok.RequiredArgsConstructor;

/**
 * JPA implementation of the VocabularyRepository interface.
 */
@Repository
@RequiredArgsConstructor
public class JpaVocabularyRepositoryImpl implements VocabularyRepository {

    private final SpringDataVocabularyRepository vocabularyRepository;
    private final VocabularyEntityMapper mapper;
    
    @Override
    public Optional<Vocabulary> findById(UUID id) {
        return vocabularyRepository.findById(id)
                .map(mapper::toDomainModel);
    }
    
    @Override
    public Optional<Vocabulary> findByHeadword(String headword) {
        return vocabularyRepository.findByHeadword(headword)
                .map(mapper::toDomainModel);
    }
    
    @Override
    public List<Vocabulary> findByHeadwordContaining(String headword) {
        return vocabularyRepository.findByHeadwordContainingIgnoreCase(headword)
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Vocabulary> searchByKeyword(String keyword) {
        return vocabularyRepository.searchByKeyword(keyword)
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Vocabulary> findByPos(PartOfSpeech pos) {
        return vocabularyRepository.findByPosOrderByHeadwordAsc(pos.name())
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Vocabulary> findSystemVocabularies() {
        return vocabularyRepository.findByIsSystemTrue()
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }
    
    @Override
    public Vocabulary save(Vocabulary vocabulary) {
        var entity = mapper.toEntity(vocabulary);
        var savedEntity = vocabularyRepository.save(entity);
        return mapper.toDomainModel(savedEntity);
    }
    
    @Override
    public void delete(Vocabulary vocabulary) {
        var entity = mapper.toEntity(vocabulary);
        vocabularyRepository.delete(entity);
    }
    
    @Override
    public List<Vocabulary> findAll() {
        return vocabularyRepository.findAll()
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }
}
