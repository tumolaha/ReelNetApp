package com.learning.reelnet.modules.vocabulary.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.learning.reelnet.common.model.base.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vocabulary_set")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class VocabularySet extends BaseEntity<UUID> {

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "view_count")
    @Builder.Default
    private Long viewCount = 0L;

    @Column(name = "like_count")
    @Builder.Default
    private Long likeCount = 0L;

    @Column(name = "share_count")
    @Builder.Default
    private Long shareCount = 0L;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    @Builder.Default
    private Visibility visibility = Visibility.PRIVATE;

    @Column(name = "difficulty_level")
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "is_system")
    @Builder.Default
    private boolean isSystem = false;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UUID> vocabularyIds = new HashSet<>();

    @OneToMany(mappedBy = "vocabularySet", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<VocabularySetItem> vocabularyItems = new HashSet<>();

    // Cần bổ sung quan hệ với Vocabulary thông qua VocabularySetItem

    public enum Visibility {
        PRIVATE, // Chỉ người tạo có thể xem
        UNLISTED, // Có link mới xem được
        PUBLIC // Ai cũng xem được
    }

    public enum DifficultyLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED,
        EXPERT
    }

    public enum Category {
        GENERAL,
        ACADEMIC,
        BUSINESS,
        TECHNOLOGY,
        MEDICAL,
        IELTS,
        TOEIC,
        TOEFL,
        TRAVEL,
        FOOD_AND_DRINK,
        SCIENCE,
        ARTS,
        SPORTS,
        ENTERTAINMENT,
        SOCIAL_MEDIA,
        CULTURE,
        HISTORY,
        GEOGRAPHY,
        LANGUAGE,
        PSYCHOLOGY,
        PHILOSOPHY,
        RELIGION,
        POLITICS,
        TECHNICAL,
        FINANCE,
        ENVIRONMENT,
        EDUCATION,
        DAILY_CONVERSATION,
        LITERATURE,
        MUSIC,
        OTHER
    }

    // Constructor with required fields
    public VocabularySet(String name, String createdBy) {
        this.name = name;
        this.createdBy = createdBy;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void incrementShareCount() {
        this.shareCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void decrementShareCount() {
        if (this.shareCount > 0) {
            this.shareCount--;
        }
    }

    public void addVocabulary(Vocabulary vocabulary, int displayOrder) {
        VocabularySetItem item = new VocabularySetItem();
        item.setVocabularySet(this);
        item.setVocabulary(vocabulary);
        item.setDisplayOrder(displayOrder);
        this.vocabularyItems.add(item);
    }

    public Set<Vocabulary> getVocabularies() {
        return vocabularyItems.stream()
            .map(VocabularySetItem::getVocabulary)
            .collect(Collectors.toSet());
    }
}
