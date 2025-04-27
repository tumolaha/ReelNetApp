package com.learning.reelnet.modules.vocabulary.application.services;

import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.common.api.query.SearchParams;
import com.learning.reelnet.common.api.query.annotation.SupportedParams;
import com.learning.reelnet.common.api.query.validator.QueryParamValidator;
import com.learning.reelnet.common.exception.ResourceNotFoundException;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.application.mapper.VocabularySetDtoMapper;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularyRepository;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularySetItemRepository;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularySetRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@SupportedParams(allowedSortFields = { "name", "createdAt", "updatedAt", "difficultyLevel" }, allowedFilterFields = {
        "category", "visibility", "difficultyLevel",
        "createdBy" }, allowedSearchFields = { "name", "description" }, maxPageSize = 50)
public class VocabularySetApplicationService {

    private final VocabularySetRepository vocabularySetRepository;
    private final VocabularySetDtoMapper vocabularySetMapper;
    private final VocabularySetItemRepository vocabularySetItemRepository;
    private final VocabularyRepository vocabularyRepository;
    private final QueryParamValidator validator;

    // region Basic CRUD Operations

    /**
     * Create a new vocabulary set
     *
     * @param vocabularySetDto Information of the vocabulary set to create
     * @return Created VocabularySet
     */
    @Transactional
    public VocabularySet createVocabularySet(VocabularySetDto vocabularySetDto) {
        log.info("Creating vocabulary set: {}", vocabularySetDto);
        VocabularySet vocabularySet = vocabularySetMapper.toEntity(vocabularySetDto);
        setDefaultValues(vocabularySet);

        return vocabularySetRepository.save(vocabularySet);
    }

    /**
     * Get vocabulary set by ID
     *
     * @param id ID of the vocabulary set
     * @return VocabularySet
     * @throws ResourceNotFoundException if set not found
     */
    @Transactional(readOnly = true)
    public VocabularySet getVocabularySetById(UUID id) {
        log.info("Getting vocabulary set with id: {}", id);
        return vocabularySetRepository.findById(id);
    }

    /**
     * Update an existing vocabulary set
     *
     * @param vocabularySetDto Update information
     * @return Updated VocabularySet
     * @throws ResourceNotFoundException if set not found
     */
    @Transactional
    public VocabularySet updateVocabularySet(VocabularySetDto vocabularySetDto) {
        log.info("Updating vocabulary set with id: {}", vocabularySetDto.getId());
        VocabularySet existingSet = getVocabularySetById(vocabularySetDto.getId());
        VocabularySet updatedSet = vocabularySetMapper.updateEntityFromDto(vocabularySetDto, existingSet);

        return vocabularySetRepository.save(updatedSet);
    }

    /**
     * Delete vocabulary set by ID
     *
     * @param id ID of the vocabulary set to delete
     * @throws ResourceNotFoundException if set not found
     */
    @Transactional
    public void deleteVocabularySet(UUID id) {
        log.info("Deleting vocabulary set with id: {}", id);

        if (!vocabularySetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vocabulary set not found with id: " + id);
        }
        vocabularySetRepository.deleteById(id);
    }

    // endregion

    // region Query Operations

    /**
     * Get all vocabulary sets with filtering and pagination
     *
     * @param queryParams  Pagination and sorting parameters
     * @param filterParams Filter criteria
     * @param searchParams Search criteria
     * @return Page of VocabularySet
     */
    @Transactional(readOnly = true)
    public Page<VocabularySet> getAllVocabularySets(
            QueryParams queryParams,
            FilterParams filterParams,
            SearchParams searchParams) {
        log.info("Getting all vocabulary sets with query: {}, filter: {}, search: {}",
                queryParams, filterParams, searchParams);
        validator.validateAll(queryParams, filterParams, searchParams, VocabularySet.class);
        return vocabularySetRepository.findAll(queryParams, filterParams, searchParams);
    }

    /**
     * Get recently used vocabulary sets for a user
     *
     * @param userId       User ID
     * @param queryParams  Pagination parameters
     * @param filterParams Filter criteria
     * @param searchParams Search criteria
     * @return Page of recent VocabularySet
     */
    @Transactional(readOnly = true)
    public Page<VocabularySet> getRecentlyUsedVocabularySets(
            String userId,
            QueryParams queryParams,
            FilterParams filterParams,
            SearchParams searchParams) {
        log.info("Getting recently used sets for user: {} with params: {}", userId, queryParams);
        validator.validateAll(queryParams, filterParams, searchParams, VocabularySet.class);
        return vocabularySetRepository.findRecentlyByUser(userId, queryParams, filterParams, searchParams);
    }

    /**
     * Find public vocabulary sets
     *
     * @param queryParams Pagination parameters
     * @return Page of public VocabularySet
     */
    @Transactional(readOnly = true)
    public Page<VocabularySet> findPublicVocabularySets(QueryParams queryParams) {
        log.info("Finding public vocabulary sets");
        FilterParams filterParams = new FilterParams();
        filterParams.addFilter("visibility", "", "PUBLIC");
        return vocabularySetRepository.findAll(queryParams, filterParams, null);
    }

    // endregion

    // region Helper Methods

    private void setDefaultValues(VocabularySet vocabularySet) {
        if (vocabularySet.getId() == null) {
            vocabularySet.setId(UUID.randomUUID());
        }

        LocalDateTime now = LocalDateTime.now();
        if (vocabularySet.getCreatedAt() == null) {
            vocabularySet.setCreatedAt(now);
        }
        vocabularySet.setUpdatedAt(now);
    }

    // endregion
}