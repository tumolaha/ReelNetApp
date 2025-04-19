package com.learning.reelnet.modules.vocabulary.application.query;

import com.learning.reelnet.common.application.cqrs.query.QueryHandler;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.api.query.GetVocabularySetByIdQuery;
import com.learning.reelnet.modules.vocabulary.application.mapper.VocabularySetMapper;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularySetRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetVocabularySetByIdQueryHandler implements QueryHandler<VocabularySetDto, GetVocabularySetByIdQuery> {
    private final VocabularySetRepository vocabularySetRepository; // Assuming you have a repository to fetch data
    private final VocabularySetMapper vocabularySetMapper; // Assuming you have a mapper to convert entities to DTOs

    @Override
    public VocabularySetDto handle(GetVocabularySetByIdQuery query) {
        // Logic to retrieve the vocabulary set by ID from the database or any other
        // source
        // For example, using a repository to fetch the data
        VocabularySet vocabularySet = vocabularySetRepository.findById(query.getId());
        return vocabularySetMapper.toDto(vocabularySet); // Convert VocabularySet to VocabularySetDto for return
    }

}
