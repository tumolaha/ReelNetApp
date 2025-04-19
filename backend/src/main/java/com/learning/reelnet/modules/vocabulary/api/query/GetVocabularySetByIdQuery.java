package com.learning.reelnet.modules.vocabulary.api.query;

import java.util.UUID;

import com.learning.reelnet.common.application.cqrs.query.Query;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class GetVocabularySetByIdQuery implements Query<VocabularySetDto> {
    private final UUID id;  
}
