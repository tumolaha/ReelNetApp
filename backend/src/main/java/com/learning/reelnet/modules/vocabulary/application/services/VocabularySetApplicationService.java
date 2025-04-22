package com.learning.reelnet.modules.vocabulary.application.services;

import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.common.api.query.SearchParams;
import com.learning.reelnet.common.api.query.annotation.SupportedParams;
import com.learning.reelnet.common.api.query.validator.QueryParamValidator;
import com.learning.reelnet.common.exception.ResourceNotFoundException;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.application.mapper.VocabularySetMapper;
import com.learning.reelnet.modules.vocabulary.domain.model.Vocabulary;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySetItem;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularyRepository;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularySetItemRepository;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularySetRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@SupportedParams(allowedSortFields = { "name", "createdAt", "updatedAt", "difficultyLevel" }, allowedFilterFields = {
        "category", "visibility", "difficultyLevel",
        "createdBy" }, allowedSearchFields = { "name", "description" }, maxPageSize = 50)
public class VocabularySetApplicationService {

    private final VocabularySetRepository vocabularySetRepository;
    private final VocabularySetMapper vocabularySetMapper;
    private final VocabularySetItemRepository vocabularySetItemRepository;
    private final VocabularyRepository vocabularyRepository;
    private final QueryParamValidator validator;

    /**
     * Create a new vocabulary set
     *
     * @param vocabularySetDto Information of the vocabulary set to create
     * @return Created VocabularySet
     */
    @Transactional
    public VocabularySet createVocabularySet(VocabularySetDto vocabularySetDto) {
        log.info("Creating vocabulary set: {}", vocabularySetDto);

        // Convert from DTO to entity
        VocabularySet vocabularySet = vocabularySetMapper.toEntity(vocabularySetDto);

        // Ensure default values
        if (vocabularySet.getId() == null) {
            vocabularySet.setId(UUID.randomUUID());
        }

        LocalDateTime now = LocalDateTime.now();
        if (vocabularySet.getCreatedAt() == null) {
            vocabularySet.setCreatedAt(now);
        }

        vocabularySet.setUpdatedAt(now);

        // Save vocabulary set
        return vocabularySetRepository.save(vocabularySet);
    }

    /**
     * Update a vocabulary set
     *
     * @param id               ID of the vocabulary set
     * @param vocabularySetDto Update information
     * @return Updated VocabularySet
     */
    @Transactional
    public VocabularySet updateVocabularySet(VocabularySetDto vocabularySetDto) {
        log.info("Updating vocabulary set with id: {}", vocabularySetDto.getId());

        // Find existing vocabulary set
        VocabularySet existingSet = vocabularySetRepository.findById(vocabularySetDto.getId());

        // Update information from DTO
        VocabularySet updatedSet = vocabularySetMapper.updateEntityFromDto(vocabularySetDto, existingSet);

        // Save updated vocabulary set
        return vocabularySetRepository.save(updatedSet);
    }

    /**
     * Get vocabulary set by ID
     *
     * @param id ID of the vocabulary set
     * @return VocabularySet
     */
    @Transactional(readOnly = true)
    public VocabularySet getVocabularySetById(UUID id) {
        log.info("Getting vocabulary set with id: {}", id);

        VocabularySet vocabularySet = vocabularySetRepository.findById(id);
        if (vocabularySet == null) {
            throw new ResourceNotFoundException("Vocabulary set not found with id: " + id);
        }

        return vocabularySet;
    }

    /**
     * Delete vocabulary set
     *
     * @param id ID of the vocabulary set
     * @return Deleted VocabularySet
     */
    @Transactional
    public void deleteVocabularySet(UUID id) {
        log.info("Deleting vocabulary set with id: {}", id);

        VocabularySet vocabularySet = vocabularySetRepository.findById(id);
        if (vocabularySet == null) {
            throw new ResourceNotFoundException("Vocabulary set not found with id: " + id);
        }

        vocabularySetRepository.deleteById(id);
    }

    /**
     * Get all vocabulary sets
     *
     * @param queryParams  Pagination and sorting information
     * @param filterParams Filter information
     * @param searchParams Search information
     * @return List of VocabularySet
     */
    @Transactional(readOnly = true)
    public Page<VocabularySet> getAllVocabularySets(
            QueryParams queryParams,
            FilterParams filterParams,
            SearchParams searchParams) {

        log.info("Getting all vocabulary sets with query: {}, filter: {}, search: {}",
                queryParams, filterParams, searchParams);
        validator.validateAll(queryParams, filterParams, searchParams, VocabularySet.class);
        Page<VocabularySet> vocabularySets = vocabularySetRepository.findAll(queryParams, filterParams, searchParams);

        // Create and return the Page object
        return vocabularySets;
    }

    /**
     * Search vocabulary sets with criteria
     *
     * @param queryParams  Pagination and sorting information
     * @param filterParams Filter information
     * @param searchParams Search information
     * @return List of VocabularySet
     */
    @Transactional(readOnly = true)
    public Page<VocabularySet> findVocabularySets(
            QueryParams queryParams,
            FilterParams filterParams,
            SearchParams searchParams) {

        log.info("Finding vocabulary sets with query: {}, filter: {}, search: {}",
                queryParams, filterParams, searchParams);
        validator.validateAll(queryParams, filterParams, searchParams, VocabularySet.class);
        Page<VocabularySet> vocabularySets = vocabularySetRepository.findAll(queryParams, filterParams, searchParams);

        return vocabularySets;
    }

    /**
     * Find vocabulary sets by creator
     *
     * @param userId ID of the creator
     * @return List of VocabularySet
     */
    @Transactional(readOnly = true)
    public List<VocabularySet> findVocabularySetsByCreatedBy(String userId) {
        log.info("Finding vocabulary sets created by user: {}", userId);

        List<VocabularySet> vocabularySets = vocabularySetRepository.findByUserId(userId);

        return vocabularySets;
    }

    /**
     * Find public vocabulary sets
     *
     * @param queryParams Pagination and sorting information
     * @return List of VocabularySet
     */
    @Transactional(readOnly = true)
    public Page<VocabularySet> findPublicVocabularySets(QueryParams queryParams) {
        log.info("Finding public vocabulary sets");

        // Create filter param for visibility=PUBLIC
        FilterParams filterParams = new FilterParams();
        filterParams.addFilter("visibility", "", "PUBLIC");

        Page<VocabularySet> vocabularySets = vocabularySetRepository.findAll(queryParams, filterParams, null);
        return vocabularySets;
    }

    /**
     * Add vocabularies to a set
     *
     * @param setId         ID of the vocabulary set
     * @param vocabularyIds List of vocabulary IDs to add
     */
    @Transactional
    public void addVocabulariesToSet(UUID setId, List<UUID> vocabularyIds) {
        VocabularySet set = vocabularySetRepository.findById(setId);

        int maxOrder = vocabularySetItemRepository.findMaxDisplayOrderBySetId(setId);

        List<VocabularySetItem> items = new ArrayList<>();
        for (int i = 0; i < vocabularyIds.size(); i++) {
            Vocabulary vocab = vocabularyRepository.getReferenceById(vocabularyIds.get(i));
            items.add(VocabularySetItem.builder()
                    .vocabularySet(set)
                    .vocabulary(vocab)
                    .displayOrder(maxOrder + i + 1)
                    .build());
        }

        vocabularySetItemRepository.saveAll(items);
    }
}