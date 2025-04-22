package com.learning.reelnet.modules.vocabulary.application.query;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.learning.reelnet.common.application.cqrs.query.QueryHandler;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.api.query.GetAllVocabularySetQuery;
import com.learning.reelnet.modules.vocabulary.application.services.VocabularySetApplicationService;

import lombok.AllArgsConstructor;


@Component("GetAllVocabularySetQueryHandler")
@AllArgsConstructor
public class GetAllVocabularySetQueryHandler implements QueryHandler<Page<VocabularySetDto>, GetAllVocabularySetQuery> {
    private final VocabularySetApplicationService vocabularySetApplicationService;

    @Override
    public Page<VocabularySetDto> handle(GetAllVocabularySetQuery query) {
        return vocabularySetApplicationService.getAllVocabularySets(query.getQueryParams(), query.getFilterParams(),
                query.getSearchParams());
    }

}
