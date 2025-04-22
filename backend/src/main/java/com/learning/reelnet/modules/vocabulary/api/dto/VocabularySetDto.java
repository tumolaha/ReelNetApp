package com.learning.reelnet.modules.vocabulary.api.dto;

import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet.Category;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet.DifficultyLevel;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet.Visibility;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Vocabulary Set Data Transfer Object")
public class VocabularySetDto {
    
    @Schema(description = "Unique identifier of the vocabulary set")
    private UUID id;
    
    @NotBlank(message = "Tên bộ từ vựng không được để trống")
    @Size(max = 255, message = "Tên bộ từ vựng không được quá 255 ký tự")
    @Schema(description = "Name of the vocabulary set", required = true)
    private String name;
    
    @Size(max = 1000, message = "Mô tả không được quá 1000 ký tự")
    @Schema(description = "Description of the vocabulary set")
    private String description;
    
    @Schema(description = "Whether the vocabulary set is active")
    private boolean isActive;
    
    @Schema(description = "ID of the user who created the vocabulary set")
    private String createdBy;
    
    @Builder.Default
    @Schema(description = "Number of views")
    private Long viewCount = 0L;
    
    @Builder.Default
    @Schema(description = "Number of likes")
    private Long likeCount = 0L;
    
    @Builder.Default
    @Schema(description = "Number of shares")
    private Long shareCount = 0L;
    
    @NotNull(message = "Visibility không được để trống")
    @Schema(description = "Visibility level of the vocabulary set", required = true)
    private Visibility visibility;
    
    @Schema(description = "Difficulty level of the vocabulary set")
    private DifficultyLevel difficultyLevel;
    
    @Schema(description = "Category of the vocabulary set")
    private Category category;
    
    @Schema(description = "Whether this is a system vocabulary set")
    private boolean isSystem;
    
    @Schema(description = "Number of vocabularies in the set")
    private int vocabularyCount;
    
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
    
    @Schema(description = "Whether the current user is the owner")
    private boolean isOwner;
    
    @Schema(description = "Whether the current user has liked this set")
    private boolean isLiked;
    
    // Nested DTO cho danh sách từ vựng trong set
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VocabularyItemDto {
        
        private UUID id;
        
        @NotBlank(message = "Từ vựng không được để trống")
        private String word;
        
        private String phoneticSpelling;
        
        @Size(max = 2000, message = "Định nghĩa không được quá 2000 ký tự")
        private String definition;
        
        @Size(max = 2000, message = "Ví dụ không được quá 2000 ký tự")
        private String example;
        
        private String audioUrl;
        
        private String imageUrl;
        
        private String partOfSpeech;
        
        private Integer displayOrder;
        
        @Size(max = 1000, message = "Định nghĩa tùy chỉnh không được quá 1000 ký tự")
        private String customDefinition;
        
        @Size(max = 1000, message = "Ví dụ tùy chỉnh không được quá 1000 ký tự")
        private String customExample;
        
        @Size(max = 500, message = "Ghi chú không được quá 500 ký tự")
        private String notes;
        
        private boolean mastered;
    }
    
    // DTO cho request tạo mới bộ từ vựng
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        
        @NotBlank(message = "Tên bộ từ vựng không được để trống")
        @Size(max = 255, message = "Tên bộ từ vựng không được quá 255 ký tự")
        private String name;
        
        @Size(max = 1000, message = "Mô tả không được quá 1000 ký tự")
        private String description;
        
        @NotNull(message = "Visibility không được để trống")
        private Visibility visibility;
        
        private DifficultyLevel difficultyLevel;
        
        private Category category;
        
        // Danh sách từ vựng ban đầu (nếu có)
        private List<UUID> vocabularyIds;
    }
    
    // DTO cho request cập nhật bộ từ vựng
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        
        @Size(max = 255, message = "Tên bộ từ vựng không được quá 255 ký tự")
        private String name;
        
        @Size(max = 1000, message = "Mô tả không được quá 1000 ký tự")
        private String description;
        
        private Boolean isActive;
        
        private Visibility visibility;
        
        private DifficultyLevel difficultyLevel;
        
        private Category category;
    }
    
    // DTO cho việc thêm từ vựng vào bộ
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddVocabularyRequest {
        
        @NotNull(message = "ID từ vựng không được để trống")
        private UUID vocabularyId;
        
        private Integer displayOrder;
        
        @Size(max = 1000, message = "Định nghĩa tùy chỉnh không được quá 1000 ký tự")
        private String customDefinition;
        
        @Size(max = 1000, message = "Ví dụ tùy chỉnh không được quá 1000 ký tự")
        private String customExample;
        
        @Size(max = 500, message = "Ghi chú không được quá 500 ký tự")
        private String notes;
    }
    
    // DTO cho phản hồi khi tìm kiếm bộ từ vựng
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResponse {
        
        private List<VocabularySetDto> content;
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean last;
    }
}
