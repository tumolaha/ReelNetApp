package com.learning.reelnet.modules.vocabulary.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyDto {
    private String word;
    private String definition;
    private String exampleSentence;
    private String partOfSpeech;
    private String synonyms;
    private String antonyms;
    private String pronunciation;
    private String createdBy;
    private String updatedBy;

    
}
