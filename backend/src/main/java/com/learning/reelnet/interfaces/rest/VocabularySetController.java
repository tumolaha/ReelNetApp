package com.learning.reelnet.interfaces.rest;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.common.api.query.SearchParams;
import com.learning.reelnet.common.api.query.utils.QueryBuilder;
import com.learning.reelnet.common.api.response.ApiResponse;
import com.learning.reelnet.common.api.response.PagedResponse;
import com.learning.reelnet.common.exception.ResourceNotFoundException;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.api.facade.VocabularySetFacade;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/vocabulary-sets")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vocabulary Sets", description = "API endpoints for managing vocabulary sets")
public class VocabularySetController {

        private final VocabularySetFacade vocabularySetFacade;

        /*
         * create a new vocabulary set
         * 
         * @param createRequest the information for creating a new vocabulary set
         * 
         * @return the created vocabulary set
         */
        @PostMapping
        @Operation(summary = "Create a new vocabulary set", description = "Creates a new vocabulary set with the provided information")
        public ApiResponse<Optional<VocabularySetDto>> createVocabularySet(
                        @Valid @RequestBody VocabularySetDto.CreateRequest createRequest) throws Exception {
                log.info("Creating a new vocabulary set");
                Optional<VocabularySetDto> newSet = vocabularySetFacade.createVocabularySet(createRequest);
                return ApiResponse.success(newSet, "Vocabulary set created successfully");
        }

        /*
         * update vocabulary set by id
         * 
         * @param id the ID of the vocabulary set to update
         * 
         * @param updateRequest the updated vocabulary set information
         * 
         * @return the updated vocabulary set
         */
        @PutMapping("/{id}")
        @Operation(summary = "Update vocabulary set", description = "Updates an existing vocabulary set if the user has access to it")
        public ApiResponse<VocabularySetDto> updateVocabularySet(
                        @PathVariable UUID id,
                        @Valid @RequestBody VocabularySetDto.UpdateRequest updateRequest) throws Exception {
                log.info("Updating vocabulary set with ID: {}", id);
                Optional<VocabularySetDto> updatedSet = vocabularySetFacade.updateVocabularySet(id, updateRequest);
                return ApiResponse.success(updatedSet.orElse(null), "Vocabulary set updated successfully");
        }

        /*
         * delete vocabulary set by id
         * 
         * @param id the ID of the vocabulary set to delete
         * 
         * @return a success message if the deletion was successful
         */
        @DeleteMapping("/{id}")
        @Operation(summary = "Delete vocabulary set", description = "Deletes a vocabulary set if the user has access to it")
        public ApiResponse<Void> deleteVocabularySet(@PathVariable UUID id) throws Exception {
                log.info("Deleting vocabulary set with ID: {}", id);
                vocabularySetFacade.deleteVocabularySet(id);
                return ApiResponse.success(null, "Vocabulary set deleted successfully");
        }

        /*
         * get vocabulary set by id
         * 
         * @param id the ID of the vocabulary set to retrieve
         * 
         * @return the vocabulary set with the specified ID
         */
        @GetMapping("/{id}")
        @Operation(summary = "Get vocabulary set by ID", description = "Retrieves a vocabulary set by its ID if the user has access to it")
        public ApiResponse<VocabularySetDto> getVocabularySetById(@PathVariable UUID id)
                        throws ResourceNotFoundException, Exception {
                log.info("Retrieving vocabulary set with ID: {}", id);
                VocabularySetDto vocabularySet = vocabularySetFacade.getVocabularySetById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Vocabulary set not found with ID: " + id));
                return ApiResponse.success(vocabularySet, "Vocabulary set retrieved successfully");
        }

        /*
         * get vocabularies in set
         * 
         * @param id the ID of the vocabulary set to retrieve vocabularies from
         * 
         * @param pageRequest pagination information
         * 
         * @return the vocabularies in the specified vocabulary set
         */
        @GetMapping
        @Operation(summary = "Get all vocabulary sets", description = "Retrieves all vocabulary sets with pagination, filtering and searching")
        @Parameters({
                        @Parameter(name = "page", description = "Page number (0-based)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
                        @Parameter(name = "size", description = "Page size", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
                        @Parameter(name = "sortBy", description = "Field to sort by", example = "name", schema = @Schema(type = "string", defaultValue = "createdAt")),
                        @Parameter(name = "sortDirection", description = "Sort direction", example = "DESC", schema = @Schema(type = "string", allowableValues = {
                                        "ASC", "DESC" }, defaultValue = "DESC")),
                        @Parameter(name = "filter", description = "Filter in JSON format", example = "{\"visibility\":\"PUBLIC\",\"category\":\"GENERAL\"}"),
                        @Parameter(name = "q", description = "Quick search query", example = "vocabulary"),
                        @Parameter(name = "searchFields", description = "Fields to search in (comma-separated)", example = "name,description"),
                        @Parameter(name = "category.eq", description = "Filter category equals", example = "GENERAL"),
                        @Parameter(name = "visibility.eq", description = "Filter visibility equals", example = "PUBLIC"),
                        @Parameter(name = "createdAt.gt", description = "Filter created after date", example = "2023-01-01"),
                        @Parameter(name = "likeCount.gte", description = "Filter minimum likes", example = "10")
        })
        public ApiResponse<PagedResponse<VocabularySetDto>> getAllVocabularySets(
                        @RequestParam(required = false) Map<String, String> allParams) throws Exception {

                // Use QueryBuilder to build query params
                Object[] params = QueryBuilder.buildQueryParams(allParams, VocabularySet.class);
                QueryParams queryParams = (QueryParams) params[0];
                FilterParams filterParams = (FilterParams) params[1];
                SearchParams searchParams = (SearchParams) params[2];

                log.info("REST request to find vocabulary sets with query: {}, filter: {}, search: {}",
                                queryParams, filterParams, searchParams);

                Page<VocabularySetDto> result = vocabularySetFacade.searchVocabularySets(
                                filterParams, queryParams, searchParams);

                PagedResponse<VocabularySetDto> pagedResponse = PagedResponse.<VocabularySetDto>builder()
                                .content(result.getContent())
                                .page(PagedResponse.PageMetadata.from(result))
                                .build();
                return ApiResponse.success(pagedResponse, "Vocabulary sets retrieved successfully");
        }

        /*
         * get Recently used vocabulary sets
         * 
         * @param page the page number (0-based) for pagination
         * 
         * @param size the number of items per page
         * 
         * @return a list of recently used vocabulary sets
         */
        @GetMapping("/recent")
        @Operation(summary = "Get recently used vocabulary sets", description = "Retrieves all recently used vocabulary sets")
        public ApiResponse<?> getRecentlyUsedVocabularySets(
                        @RequestParam(required = false) Map<String, String> allParams) throws Exception {

                Object[] params = QueryBuilder.buildQueryParams(allParams, VocabularySet.class);
                QueryParams queryParams = (QueryParams) params[0];
                FilterParams filterParams = (FilterParams) params[1];
                SearchParams searchParams = (SearchParams) params[2];
                Page<VocabularySetDto> recentSets = vocabularySetFacade.getVocabularySetHistoryByUserId(
                                "By google-oauth2|106200961462234067141", queryParams, filterParams, searchParams);
                PagedResponse<VocabularySetDto> pagedResponse = PagedResponse.<VocabularySetDto>builder()
                                .content(recentSets.getContent())
                                .page(PagedResponse.PageMetadata.from(recentSets))
                                .build();

                return ApiResponse.success(pagedResponse, "Recently used vocabulary sets retrieved successfully");
        }
}
