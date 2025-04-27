package com.learning.reelnet.modules.vocabulary.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.learning.reelnet.modules.vocabulary.domain.model.Vocabulary;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.PartOfSpeech;

/**
 * Repository interface for Vocabulary domain model.
 */
public interface VocabularyRepository {
    
    /**
     * Find a vocabulary by its ID
     * 
     * @param vocabularyIds The UUID of the vocabulary
     * @return An Optional containing the vocabulary if found, empty otherwise
     */
    Optional<Vocabulary> findById(UUID vocabularyIds);
    
    /**
     * Find a vocabulary by its headword
     * 
     * @param headword The headword to search for
     * @return An Optional containing the vocabulary if found, empty otherwise
     */
    Optional<Vocabulary> findByHeadword(String headword);
    
    /**
     * Find vocabularies by partial headword match
     * 
     * @param headword The partial headword to search for
     * @return A list of matching vocabularies
     */
    List<Vocabulary> findByHeadwordContaining(String headword);
    
    /**
     * Search vocabularies by keyword
     * 
     * @param keyword The keyword to search for
     * @return A list of matching vocabularies
     */
    List<Vocabulary> searchByKeyword(String keyword);
    
    /**
     * Find vocabularies by part of speech
     * 
     * @param pos The part of speech to filter by
     * @return A list of matching vocabularies
     */
    List<Vocabulary> findByPos(PartOfSpeech pos);
    
    /**
     * Find system vocabularies
     * 
     * @return A list of system vocabularies
     */
    List<Vocabulary> findSystemVocabularies();
    
    /**
     * Save a vocabulary
     * 
     * @param vocabulary The vocabulary to save
     * @return The saved vocabulary
     */
    Vocabulary save(Vocabulary vocabulary);
    
    /**
     * Delete a vocabulary
     * 
     * @param vocabulary The vocabulary to delete
     */
    void delete(Vocabulary vocabulary);
    
    /**
     * Find all vocabularies
     * 
     * @return A list of all vocabularies
     */
    List<Vocabulary> findAll();
}
