package com.learning.reelnet.modules.vocabulary.domain.valueobject;

/**
 * Value object representing the difficulty level of a VocabularySet.
 * This helps users select appropriate vocabulary sets based on their language proficiency.
 */
public enum DifficultyLevel {
    BEGINNER("Dành cho người mới bắt đầu"),
    INTERMEDIATE("Trình độ trung bình"),
    ADVANCED("Trình độ nâng cao"),
    EXPERT("Trình độ chuyên gia");

    private final String description;

    DifficultyLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
