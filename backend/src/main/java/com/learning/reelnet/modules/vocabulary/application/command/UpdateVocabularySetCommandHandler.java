package com.learning.reelnet.modules.vocabulary.application.command;

import org.springframework.stereotype.Component;

import com.learning.reelnet.common.application.cqrs.command.CommandHandler;
import com.learning.reelnet.modules.vocabulary.api.command.UpdateVocabularySetCommand;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.application.services.VocabularyApplicationService;
import com.learning.reelnet.modules.vocabulary.application.services.VocabularySetApplicationService;

import lombok.RequiredArgsConstructor;

@Component("UpdateVocabularySetCommandHandler")
@RequiredArgsConstructor
public class UpdateVocabularySetCommandHandler implements CommandHandler<Boolean, UpdateVocabularySetCommand> {
    final VocabularySetApplicationService vocabularySetApplicationService;
    final VocabularyApplicationService vocabularyApplicationService;

    @Override
    public Boolean handle(UpdateVocabularySetCommand command) {
        VocabularySetDto vocabularySetDto = VocabularySetDto.builder()
                .id(command.getId())
                .name(command.getName())
                .description(command.getDescription())
                .visibility(command.getVisibility())
                .difficultyLevel(command.getDifficultyLevel())
                .category(command.getCategory())
                .build();
        vocabularySetApplicationService.updateVocabularySet(vocabularySetDto);
        return true;
    }
}
