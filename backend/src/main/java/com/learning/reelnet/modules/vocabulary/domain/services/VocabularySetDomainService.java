package com.learning.reelnet.modules.vocabulary.domain.services;

import com.learning.reelnet.modules.vocabulary.domain.model.Vocabulary;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.Category;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.DifficultyLevel;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.Visibility;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Domain Service handling complex business logic related to VocabularySet
 * Does not interact with Repository but only works with domain objects
 */
@Service
public class VocabularySetDomainService {
    
    /**
     * Check if user has access rights to vocabulary set
     * 
     * @param vocabularySet VocabularySet to check
     * @param userId ID of the user
     * @return true if access is granted
     */
    public boolean canUserAccessVocabularySet(VocabularySet vocabularySet, String userId) {
        // Creator always has access rights
        if (userId.equals(vocabularySet.getCreatedBy())) {
            return true;
        }
          // If PUBLIC, everyone can access
        if (Visibility.PUBLIC.equals(vocabularySet.getVisibility())) {
            return true;
        }
        
        // Logic to check SHARED (can be extended later)
        if (Visibility.UNLISTED.equals(vocabularySet.getVisibility())) {
            // Check sharing permissions
            return false; // Temporarily return false, additional logic needed
        }
        
        return false;
    }
      /**
     * Evaluate difficulty level of vocabulary set based on its words
     * 
     * @param vocabularySet VocabularySet to evaluate
     * @param vocabularyItems List of vocabulary items in the set
     * @return Suggested difficulty level
     */
    public DifficultyLevel evaluateDifficulty(
            VocabularySet vocabularySet, 
            List<Vocabulary> vocabularyItems) {
        
        if (vocabularyItems == null || vocabularyItems.isEmpty()) {
            return DifficultyLevel.BEGINNER;
        }
        
        // Calculate complexity based on vocabulary
        double complexityScore = calculateComplexityScore(vocabularyItems);
        
        // Determine difficulty level based on complexity score
        if (complexityScore > 8.0) {
            return DifficultyLevel.EXPERT;
        } else if (complexityScore > 6.0) {
            return DifficultyLevel.ADVANCED;
        } else if (complexityScore > 4.0) {
            return DifficultyLevel.INTERMEDIATE;
        } else {
            return DifficultyLevel.BEGINNER;
        }
    }
    
    /**
     * Calculate complexity score of vocabulary list
     */
    private double calculateComplexityScore(List<Vocabulary> vocabularyItems) {
        int totalLength = 0;
        int totalExamples = 0;
        
        for (Vocabulary item : vocabularyItems) {
            totalLength += item.getHeadword().length(); // Length of vocabulary word
            if (item.getExamples() != null) {
                totalExamples += item.getExamples().size();
            }
        }
        
        double avgLength = (double) totalLength / vocabularyItems.size();
        double avgExamples = (double) totalExamples / vocabularyItems.size();
        
        // Formula to calculate complexity score (example only)
        return (avgLength * 0.7) + (3.0 / (avgExamples + 1)) * 3.0;
    }
      /**
     * Create a suggested vocabulary set based on keyword
     * 
     * @param keyword Keyword
     * @param userId ID of requesting user
     * @return Newly created VocabularySet (not saved)
     */
    public VocabularySet createSuggestedVocabularySet(String keyword, String userId) {
        String name = "Suggested: " + keyword;
        
        VocabularySet vocabularySet = VocabularySet.builder()
                .name(name)
                .description("Auto-generated vocabulary set based on keyword: " + keyword)
                .visibility(Visibility.PRIVATE)
                .difficultyLevel(DifficultyLevel.INTERMEDIATE)
                .category(determineCategory(keyword))
                .createdBy(userId)
                .build();
        
        vocabularySet.setId(UUID.randomUUID());
        vocabularySet.setCreatedAt(LocalDateTime.now());
        vocabularySet.setUpdatedAt(LocalDateTime.now());
        
        return vocabularySet;
    }
    
    /**
     * Determine category based on keyword
     */
    private Category determineCategory(String keyword) {
        keyword = keyword.toLowerCase();
        
        if (keyword.contains("business") || keyword.contains("company") || keyword.contains("marketing")) {
            return Category.BUSINESS;
        } else if (keyword.contains("school") || keyword.contains("study") || keyword.contains("academic")) {
            return Category.ACADEMIC;
        } else if (keyword.contains("tech") || keyword.contains("computer") || keyword.contains("programming")) {
            return Category.TECHNOLOGY;  // Note: Changed from TECHNICAL to TECHNOLOGY to match the enum
        } else if (keyword.contains("travel") || keyword.contains("vacation") || keyword.contains("tourism")) {
            return Category.TRAVEL;
        } else {
            return Category.GENERAL;
        }
    }
    
    /**
     * Analyze overlap (common vocabulary) between vocabulary sets
     * Helps users find suitable supplementary vocabulary sets
     * 
     * @param primarySet Primary vocabulary set currently in use
     * @param candidateSets Potential vocabulary sets
     * @return Vocabulary set with least overlap (best complement)
     */
    public VocabularySet findBestComplementarySet(
            VocabularySet primarySet, 
            List<VocabularySet> candidateSets) {
        
        if (candidateSets == null || candidateSets.isEmpty()) {
            return null;
        }
        
        // Calculate overlap between primary set and candidate sets
        VocabularySet bestSet = null;
        double lowestOverlapRatio = 1.0; // 100% overlap
        
        for (VocabularySet candidate : candidateSets) {
            double overlapRatio = calculateOverlapRatio(primarySet, candidate);
            
            if (overlapRatio < lowestOverlapRatio) {
                lowestOverlapRatio = overlapRatio;
                bestSet = candidate;
            }
        }
        
        return bestSet;
    }
    
    /**
     * Calculate overlap ratio between two vocabulary sets
     * 
     * @return Ratio from 0.0 (no overlap) to 1.0 (complete overlap)
     */
    private double calculateOverlapRatio(VocabularySet set1, VocabularySet set2) {
        if (set1 == null || set2 == null || 
            set1.getVocabularyItems() == null || set2.getVocabularyItems() == null ||
            set1.getVocabularyItems().isEmpty() || set2.getVocabularyItems().isEmpty()) {
            return 0.0;
        }
        
        // Get vocabulary IDs from VocabularySetItems
        Set<UUID> ids1 = set1.getVocabularyItems().stream()
            .map(item -> item.getVocabulary().getId())
            .collect(Collectors.toSet());
            
        Set<UUID> ids2 = set2.getVocabularyItems().stream()
            .map(item -> item.getVocabulary().getId())
            .collect(Collectors.toSet());
        
        // Find common vocabulary
        Set<UUID> intersection = new HashSet<>(ids1);
        intersection.retainAll(ids2);
        
        // Calculate ratio
        return (double) intersection.size() / Math.min(ids1.size(), ids2.size());
    }
}
