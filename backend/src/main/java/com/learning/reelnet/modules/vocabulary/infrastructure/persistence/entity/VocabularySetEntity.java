package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.learning.reelnet.common.api.query.annotation.SupportedParams;
import com.learning.reelnet.common.model.base.BaseEntity;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.Category;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.DifficultyLevel;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.Visibility;

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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vocabulary_set")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SupportedParams(
    allowedSortFields = {"id", "name", "createdAt", "updatedAt", "viewCount", "likeCount", "difficultyLevel"},
    allowedFilterFields = {"visibility", "category", "difficultyLevel", "createdBy", "isActive", "isSystem"},
    allowedSearchFields = {"name", "description"},
    maxPageSize = 50
)
public class VocabularySetEntity extends BaseEntity<UUID> {


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

    @OneToMany(mappedBy = "vocabularySet", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<VocabularySetItemEntity> vocabularyItems = new HashSet<>();
   

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

    
}
