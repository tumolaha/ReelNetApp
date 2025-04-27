package com.learning.reelnet.modules.vocabulary.application.query;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.learning.reelnet.common.application.cqrs.query.QueryHandler;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.api.query.GetAllVocabularySetQuery;
import com.learning.reelnet.modules.vocabulary.application.mapper.VocabularySetDtoMapper;
import com.learning.reelnet.modules.vocabulary.application.services.VocabularySetApplicationService;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;

import lombok.AllArgsConstructor;


@Component("GetAllVocabularySetQueryHandler")
@AllArgsConstructor
public class GetAllVocabularySetQueryHandler implements QueryHandler<Page<VocabularySetDto>, GetAllVocabularySetQuery> {
    private final VocabularySetApplicationService vocabularySetApplicationService;
    private final VocabularySetDtoMapper vocabularySetMapper;
    @Override
    public Page<VocabularySetDto> handle(GetAllVocabularySetQuery query) {
        
        
        
        Page<VocabularySet> vocabularySets = vocabularySetApplicationService.getAllVocabularySets(query.getQueryParams(), query.getFilterParams(),
                query.getSearchParams());
        return vocabularySetMapper.toDtoPage(vocabularySets);
    }

}
