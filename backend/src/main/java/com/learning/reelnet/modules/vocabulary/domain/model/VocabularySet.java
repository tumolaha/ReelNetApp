package com.learning.reelnet.modules.vocabulary.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.learning.reelnet.common.api.query.annotation.SupportedParams;
import com.learning.reelnet.common.model.base.BaseEntity;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.Category;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.DifficultyLevel;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.Visibility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@SupportedParams(
    allowedSortFields = {"id", "name", "createdAt", "updatedAt", "viewCount", "likeCount", "difficultyLevel"},
    allowedFilterFields = {"visibility", "category", "difficultyLevel", "createdBy", "isActive", "isSystem"},
    allowedSearchFields = {"name", "description"},
    maxPageSize = 50
)
public class VocabularySet extends BaseEntity<UUID> {

    private String name;

    private String description;

    @Builder.Default
    private boolean isActive = true;

    private String createdBy;

    @Builder.Default
    private Long viewCount = 0L;

    @Builder.Default
    private Long likeCount = 0L;

    @Builder.Default
    private Long shareCount = 0L;
    @Builder.Default
    private Visibility visibility = Visibility.PRIVATE;

    private DifficultyLevel difficultyLevel;

    private Category category;


    @Builder.Default
    private boolean isSystem = false;

    @Builder.Default
    private Set<VocabularySetItem> vocabularyItems = new HashSet<>();

    // Cần bổ sung quan hệ với Vocabulary thông qua VocabularySetItem

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
