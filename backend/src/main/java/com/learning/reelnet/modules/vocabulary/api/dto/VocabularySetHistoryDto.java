package com.learning.reelnet.modules.vocabulary.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Vocabulary Set History Data Transfer Object")
@Builder
@Data
@AllArgsConstructor
public class VocabularySetHistoryDto {
    @Schema(description = "ID of the vocabulary set")
    private String vocabularySetId;

    @Schema(description = "Name of the vocabulary set")
    private String vocabularySetName;

    @Schema(description = "Last accessed date and time")
    private String lastAccessedAt;
    
}
