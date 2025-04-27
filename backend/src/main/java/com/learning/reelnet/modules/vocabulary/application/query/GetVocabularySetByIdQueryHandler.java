package com.learning.reelnet.modules.vocabulary.application.query;

import org.springframework.stereotype.Component;

import com.learning.reelnet.common.application.cqrs.query.QueryHandler;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.api.query.GetVocabularySetByIdQuery;
import com.learning.reelnet.modules.vocabulary.application.mapper.VocabularySetDtoMapper;
import com.learning.reelnet.modules.vocabulary.application.services.VocabularySetApplicationService;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;

import lombok.RequiredArgsConstructor;

@Component("GetVocabularySetByIdQueryHandler")
@RequiredArgsConstructor
public class GetVocabularySetByIdQueryHandler implements QueryHandler<VocabularySetDto, GetVocabularySetByIdQuery> {
    
    private final VocabularySetApplicationService vocabularySetApplicationService;
    private final VocabularySetDtoMapper vocabularySetMapper;

    @Override
    public VocabularySetDto handle(GetVocabularySetByIdQuery query) {
        VocabularySet vocabularySet = vocabularySetApplicationService.getVocabularySetById(query.getId());
        
        
        
        if (vocabularySet == null) {
            return null;
        }
        return vocabularySetMapper.toDto(vocabularySet);
    }
}
