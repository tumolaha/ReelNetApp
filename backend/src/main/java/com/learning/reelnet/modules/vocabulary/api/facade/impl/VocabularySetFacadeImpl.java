package com.learning.reelnet.modules.vocabulary.api.facade.impl;

import com.learning.reelnet.common.application.cqrs.command.CommandBus;
import com.learning.reelnet.common.application.cqrs.query.QueryBus;
import com.learning.reelnet.modules.vocabulary.api.command.CreateVocabularySetCommand;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto.AddVocabularyRequest;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto.UpdateRequest;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto.VocabularyItemDto;
import com.learning.reelnet.modules.vocabulary.api.facade.VocabularySetFacade;
import com.learning.reelnet.modules.vocabulary.api.query.GetVocabularySetByIdQuery;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet.Category;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet.DifficultyLevel;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet.Visibility;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VocabularySetFacadeImpl implements VocabularySetFacade {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @Override
    public Optional<VocabularySetDto> createVocabularySet(VocabularySetDto.CreateRequest createRequest) throws Exception {
        // Lấy thông tin người dùng hiện tại

        // Chuyển đổi từ DTO sang Command
        CreateVocabularySetCommand command = CreateVocabularySetCommand.builder().build();
        
        UUID id = commandBus.dispatch(command);
        return getVocabularySetById(id);
    }

    @Override
    public Optional<VocabularySetDto> getVocabularySetById(UUID id) throws Exception {
        // Tạo query cho việc lấy bộ từ vựng
        GetVocabularySetByIdQuery query = new GetVocabularySetByIdQuery(id);
        VocabularySetDto vocabularySetDto = queryBus.dispatch(query);
        if (vocabularySetDto == null) {
            return Optional.empty(); // Trả về Optional.empty() nếu không tìm thấy bộ từ vựng
        }
        // Gửi query thông qua QueryBus
        return Optional.of(vocabularySetDto); // Trả về Optional chứa VocabularySetDto nếu tìm thấy
    }

    @Override
    public Optional<VocabularySetDto> updateVocabularySet(UUID id, UpdateRequest updateRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateVocabularySet'");
    }

    @Override
    public boolean deleteVocabularySet(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteVocabularySet'");
    }

    @Override
    public Page<VocabularySetDto> searchVocabularySets(String query, Category category, DifficultyLevel difficulty,
            Visibility visibility, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchVocabularySets'");
    }

    @Override
    public Page<VocabularySetDto> getMyVocabularySets(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMyVocabularySets'");
    }

    @Override
    public Page<VocabularySetDto> getPublicVocabularySets(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPublicVocabularySets'");
    }

    @Override
    public boolean addVocabularyToSet(UUID setId, AddVocabularyRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addVocabularyToSet'");
    }

    @Override
    public boolean removeVocabularyFromSet(UUID setId, UUID vocabularyId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeVocabularyFromSet'");
    }

    @Override
    public Page<VocabularyItemDto> getVocabulariesInSet(UUID setId, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVocabulariesInSet'");
    }

    @Override
    public boolean incrementViewCount(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'incrementViewCount'");
    }

    @Override
    public boolean toggleLike(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toggleLike'");
    }

    @Override
    public boolean isLikedByUser(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isLikedByUser'");
    }

    @Override
    public Page<VocabularySetDto> getVocabularySetsByCategory(Category category, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVocabularySetsByCategory'");
    }

    @Override
    public Page<VocabularySetDto> getVocabularySetsByDifficulty(DifficultyLevel difficultyLevel, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVocabularySetsByDifficulty'");
    }

    @Override
    public Page<VocabularySetDto> getVocabularySetsByPopularity(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVocabularySetsByPopularity'");
    }
}