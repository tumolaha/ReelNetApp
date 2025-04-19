package com.learning.reelnet.modules.vocabulary.application.services;

import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.common.api.query.SearchParams;
import com.learning.reelnet.common.api.query.annotation.SupportedParams;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@SupportedParams(allowedSortFields = { "name", "createdAt", "updatedAt", "difficultyLevel" }, allowedFilterFields = {
        "category", "visibility", "difficultyLevel",
        "createdBy" }, allowedSearchFields = { "name", "description" }, maxPageSize = 50)
public class VocabularySetApplicationService {

    private final VocabularySetRepository vocabularySetRepository;
    private final VocabularySetMapper vocabularySetMapper;
    private final VocabularySetItemRepository vocabularySetItemRepository;
    private final VocabularyRepository vocabularyRepository;

    /**
     * Create a new vocabulary set
     *
     * @param vocabularySetDto Information of the vocabulary set to create
     * @return Created VocabularySetDto
     */
    public VocabularySetDto createVocabularySet(VocabularySetDto vocabularySetDto) {
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
        VocabularySet savedSet = vocabularySetRepository.save(vocabularySet);

        // Convert result back to DTO
        return vocabularySetMapper.toDto(savedSet);
    }

    /**
     * Update a vocabulary set
     *
     * @param id ID of the vocabulary set
     * @param vocabularySetDto Update information
     * @return Updated VocabularySetDto
     */
    public VocabularySetDto updateVocabularySet(UUID id, VocabularySetDto vocabularySetDto) {
        log.info("Updating vocabulary set with id: {}", id);

        // Find existing vocabulary set
        VocabularySet existingSet = vocabularySetRepository.findById(id);

        // Update information from DTO
        VocabularySet updatedSet = vocabularySetMapper.updateEntityFromDto(vocabularySetDto, existingSet);

        // Save updated vocabulary set
        VocabularySet savedSet = vocabularySetRepository.save(updatedSet);

        // Convert result back to DTO
        return vocabularySetMapper.toDto(savedSet);
    }

    /**
     * Get vocabulary set by ID
     *
     * @param id ID of the vocabulary set
     * @return VocabularySetDto
     */
    @Transactional(readOnly = true)
    public VocabularySetDto getVocabularySetById(UUID id) {
        log.info("Getting vocabulary set with id: {}", id);

        VocabularySet vocabularySet = vocabularySetRepository.findById(id);
        if (vocabularySet == null) {
            throw new ResourceNotFoundException("Vocabulary set not found with id: " + id);
        }

        return vocabularySetMapper.toDto(vocabularySet);
    }

    /**
     * Delete vocabulary set
     *
     * @param id     ID of the vocabulary set
     * @param userId ID of the user requesting deletion
     */
    public void deleteVocabularySet(UUID id, String userId) {
        log.info("Deleting vocabulary set with id: {} by user: {}", id, userId);

        VocabularySet vocabularySet = vocabularySetRepository.findById(id);
        if (vocabularySet == null) {
            throw new ResourceNotFoundException("Vocabulary set not found with id: " + id);
        }

        // Check deletion permissions
        if (!vocabularySet.getCreatedBy().equals(userId)) {
            throw new SecurityException("You don't have permission to delete this vocabulary set");
        }

        vocabularySetRepository.deleteById(id);
    }

    /**
     * Search vocabulary sets with criteria
     *
     * @param queryParams  Pagination and sorting information
     * @param filterParams Filter information
     * @param searchParams Search information
     * @return List of VocabularySetDto
     */
    @Transactional(readOnly = true)
    public List<VocabularySetDto> findVocabularySets(
            QueryParams queryParams,
            FilterParams filterParams,
            SearchParams searchParams) {

        log.info("Finding vocabulary sets with query: {}, filter: {}, search: {}",
                queryParams, filterParams, searchParams);

        List<VocabularySet> vocabularySets = vocabularySetRepository.findAll(queryParams, filterParams, searchParams);

        return vocabularySetMapper.toDtoList(vocabularySets);
    }

    /**
     * Find vocabulary sets by creator
     *
     * @param userId ID of the creator
     * @return List of VocabularySetDto
     */
    @Transactional(readOnly = true)
    public List<VocabularySetDto> findVocabularySetsByCreatedBy(String userId) {
        log.info("Finding vocabulary sets created by user: {}", userId);

        List<VocabularySet> vocabularySets = vocabularySetRepository.findByUserId(userId);

        return vocabularySetMapper.toDtoList(vocabularySets);
    }

    /**
     * Find public vocabulary sets
     *
     * @param queryParams Pagination and sorting information
     * @return List of VocabularySetDto
     */
    @Transactional(readOnly = true)
    public List<VocabularySetDto> findPublicVocabularySets(QueryParams queryParams) {
        log.info("Finding public vocabulary sets");

        // Create filter param for visibility=PUBLIC
        FilterParams filterParams = new FilterParams();
        filterParams.addFilter("visibility", FilterParams.FilterOperation.EQ, "PUBLIC");

        List<VocabularySet> vocabularySets = vocabularySetRepository.findAll(queryParams, filterParams, null);

        return vocabularySetMapper.toDtoList(vocabularySets);
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