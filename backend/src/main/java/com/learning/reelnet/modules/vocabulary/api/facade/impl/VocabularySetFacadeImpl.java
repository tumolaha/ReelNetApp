package com.learning.reelnet.modules.vocabulary.api.facade.impl;

import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.common.api.query.SearchParams;
import com.learning.reelnet.common.application.cqrs.command.CommandBus;
import com.learning.reelnet.common.application.cqrs.query.QueryBus;
import com.learning.reelnet.modules.vocabulary.api.command.CreateVocabularySetCommand;
import com.learning.reelnet.modules.vocabulary.api.command.DeleteVocabularySetCommand;
import com.learning.reelnet.modules.vocabulary.api.command.UpdateVocabularySetCommand;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.api.facade.VocabularySetFacade;
import com.learning.reelnet.modules.vocabulary.api.query.GetVocabularySetByIdQuery;
import com.learning.reelnet.modules.vocabulary.api.query.GetAllVocabularySetQuery;
import com.learning.reelnet.modules.vocabulary.api.query.GetRecentlyVocabularySetHistoryQuery;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component("vocabularySetFacade")
@RequiredArgsConstructor
public class VocabularySetFacadeImpl implements VocabularySetFacade {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @Override
    public Optional<VocabularySetDto> createVocabularySet(VocabularySetDto.CreateRequest createRequest)
            throws Exception {
        CreateVocabularySetCommand command = CreateVocabularySetCommand.builder()
                .name(createRequest.getName())
                .description(createRequest.getDescription())
                .visibility(createRequest.getVisibility())
                .difficultyLevel(createRequest.getDifficultyLevel())
                .category(createRequest.getCategory())
                .vocabularyIds(createRequest.getVocabularyIds())
                .createdBy("google-oauth2|106200961462234067141") // TODO: Get from security context
                .build();

        UUID id = commandBus.dispatch(command);
        return getVocabularySetById(id);
    }

    @Override
    public Optional<VocabularySetDto> updateVocabularySet(UUID id, VocabularySetDto.UpdateRequest updateRequest)
            throws Exception {
        UpdateVocabularySetCommand command = UpdateVocabularySetCommand.builder()
                .id(id)
                .name(updateRequest.getName())
                .description(updateRequest.getDescription())
                .visibility(updateRequest.getVisibility())
                .difficultyLevel(updateRequest.getDifficultyLevel())
                .category(updateRequest.getCategory())
                .build();

        commandBus.dispatch(command);
        return getVocabularySetById(id);

    }

    @Override
    public Boolean deleteVocabularySet(UUID id) throws Exception {
        DeleteVocabularySetCommand command = DeleteVocabularySetCommand.builder()
                .id(id)
                .build();

        return commandBus.dispatch(command);
    }

    @Override
    public Optional<VocabularySetDto> getVocabularySetById(UUID id) throws Exception {
        GetVocabularySetByIdQuery query = new GetVocabularySetByIdQuery(id);
        VocabularySetDto vocabularySetDto = queryBus.dispatch(query);
        if (vocabularySetDto == null) {
            return Optional.empty();
        }
        return Optional.of(vocabularySetDto);
    }

    @Override
    public Page<VocabularySetDto> searchVocabularySets(FilterParams filterParams, QueryParams queryParams,
            SearchParams searchParams) throws Exception {
        GetAllVocabularySetQuery query = new GetAllVocabularySetQuery(queryParams, filterParams, searchParams);
        return queryBus.dispatch(query);
    }

    @Override
    public Page<VocabularySetDto> getVocabularySetHistoryByUserId(String userId, QueryParams queryParams,
            FilterParams filterParams, SearchParams searchParams) throws Exception {
        GetRecentlyVocabularySetHistoryQuery query = new GetRecentlyVocabularySetHistoryQuery(userId, queryParams, filterParams, searchParams);
        return queryBus.dispatch(query);
    }

}