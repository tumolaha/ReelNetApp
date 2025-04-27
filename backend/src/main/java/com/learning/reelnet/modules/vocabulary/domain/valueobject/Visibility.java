package com.learning.reelnet.modules.vocabulary.domain.valueobject;

/**
 * Value object representing the visibility level of a VocabularySet.
 * This determines who can view the vocabulary set.
 */
public enum Visibility {
    PRIVATE("Chỉ người tạo có thể xem"),
    UNLISTED("Có link mới xem được"),
    PUBLIC("Ai cũng xem được");

    private final String description;

    Visibility(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
