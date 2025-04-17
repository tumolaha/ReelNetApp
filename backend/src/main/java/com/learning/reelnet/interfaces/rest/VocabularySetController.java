package com.learning.reelnet.interfaces.rest;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.learning.reelnet.common.api.response.ApiResponse;
import com.learning.reelnet.common.exception.ResourceNotFoundException;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.api.facade.VocabularySetFacade;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet.Category;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet.DifficultyLevel;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet.Visibility;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/vocabulary-sets")
@RequiredArgsConstructor
@Slf4j
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
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create a new vocabulary set", description = "Creates a new vocabulary set with the provided information")
    public ApiResponse<VocabularySetDto> createVocabularySet(
            @Valid @RequestBody VocabularySetDto.CreateRequest createRequest) {
        log.info("Creating a new vocabulary set");
        VocabularySetDto newSet = vocabularySetFacade.createVocabularySet(createRequest);
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
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update vocabulary set", description = "Updates an existing vocabulary set if the user has access to it")
    public ApiResponse<VocabularySetDto> updateVocabularySet(
            @PathVariable UUID id,
            @Valid @RequestBody VocabularySetDto.UpdateRequest updateRequest) {
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
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete vocabulary set", description = "Deletes a vocabulary set if the user has access to it")
    public ApiResponse<Void> deleteVocabularySet(@PathVariable UUID id) {
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
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get vocabulary set by ID", description = "Retrieves a vocabulary set by its ID if the user has access to it")
    public ApiResponse<VocabularySetDto> getVocabularySetById(@PathVariable UUID id) {
        log.info("Retrieving vocabulary set with ID: {}", id);
        VocabularySetDto vocabularySet = vocabularySetFacade.getVocabularySetById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vocabulary set not found with ID: " + id));
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
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all vocabulary sets", description = "Retrieves all vocabulary sets with pagination")
    public ApiResponse<?> getAllVocabularySets(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) DifficultyLevel difficulty,
            @RequestParam(required = false) Visibility visibility,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        log.info("Retrieving all vocabulary sets with query: {}, category: {}, difficulty: {}, visibility: {}",
                query, category, difficulty, visibility);

        var result = vocabularySetFacade.searchVocabularySets(
                query, category, difficulty, visibility,
                null); // TODO: implement a proper PageRequest

        return ApiResponse.success(result, "Vocabulary sets retrieved successfully");
    }
    /*
     * add vocabulary to set
     * 
     * @param id the ID of the vocabulary set to add a vocabulary to
     * 
     * @param request the request containing the vocabulary information to add
     * 
     * @return a success message if the addition was successful
     */
    @GetMapping("/my-sets")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get user's vocabulary sets", description = "Retrieves all vocabulary sets created by the current user")
    public ApiResponse<?> getMyVocabularySets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        
        log.info("Retrieving vocabulary sets for current user");
        
        // var result = vocabularySetFacade.getMyVocabularySets(null); // TODO: implement a proper PageRequest
        return ApiResponse.success(null, "User vocabulary sets retrieved successfully");
    }
    /*
     * get public vocabulary sets
     * 
     * @param pageRequest pagination information
     * 
     * @return the public vocabulary sets
     */
    @GetMapping("/public")
    @Operation(summary = "Get public vocabulary sets", description = "Retrieves all public vocabulary sets")
    public ApiResponse<?> getPublicVocabularySets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        
        log.info("Retrieving public vocabulary sets");
        
        var result = vocabularySetFacade.getPublicVocabularySets(null); // TODO: implement a proper PageRequest
        return ApiResponse.success(result, "Public vocabulary sets retrieved successfully");
    }
}
