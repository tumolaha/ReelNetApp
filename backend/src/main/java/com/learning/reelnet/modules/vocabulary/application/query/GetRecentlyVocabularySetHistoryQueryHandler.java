package com.learning.reelnet.modules.vocabulary.application.query;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.learning.reelnet.common.application.cqrs.query.QueryHandler;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.api.query.GetRecentlyVocabularySetHistoryQuery;
import com.learning.reelnet.modules.vocabulary.application.mapper.VocabularySetMapper;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularySetRepository;

import lombok.AllArgsConstructor;


@Component("GetRecentlyVocabularySetHistoryQueryHandler")
@AllArgsConstructor
public class GetRecentlyVocabularySetHistoryQueryHandler implements QueryHandler<Page<VocabularySetDto>, GetRecentlyVocabularySetHistoryQuery> {

    private final VocabularySetMapper vocabularySetMapper;
    private final VocabularySetRepository vocabularySetRepository;

    

    @Override
    public Page<VocabularySetDto> handle(GetRecentlyVocabularySetHistoryQuery query) {
        return vocabularySetMapper.toDtoPage(
                vocabularySetRepository.findRecentlyByUser(query.getUserId(), query.getQueryParams(), query.getFilterParams(), query.getSearchParams()));
    }
    
}
