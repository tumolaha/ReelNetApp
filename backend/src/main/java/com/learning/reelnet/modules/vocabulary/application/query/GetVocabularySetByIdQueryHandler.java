package com.learning.reelnet.modules.vocabulary.application.query;

import org.springframework.stereotype.Component;

import com.learning.reelnet.common.application.cqrs.query.QueryHandler;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.api.query.GetVocabularySetByIdQuery;
import com.learning.reelnet.modules.vocabulary.application.mapper.VocabularySetMapper;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularySetRepository;

import lombok.RequiredArgsConstructor;

@Component("GetVocabularySetByIdQueryHandler")
@RequiredArgsConstructor
public class GetVocabularySetByIdQueryHandler implements QueryHandler<VocabularySetDto, GetVocabularySetByIdQuery> {
    
    private final VocabularySetRepository vocabularySetRepository;
    private final VocabularySetMapper vocabularySetMapper;

    @Override
    public VocabularySetDto handle(GetVocabularySetByIdQuery query) {
        VocabularySet vocabularySet = vocabularySetRepository.findById(query.getId());
        if (vocabularySet == null) {
            return null;
        }
        return vocabularySetMapper.toDto(vocabularySet);
    }
}
