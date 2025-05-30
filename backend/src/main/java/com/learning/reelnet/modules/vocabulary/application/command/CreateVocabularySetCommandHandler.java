package com.learning.reelnet.modules.vocabulary.application.command;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.learning.reelnet.common.application.cqrs.command.CommandHandler;
import com.learning.reelnet.modules.vocabulary.api.command.CreateVocabularySetCommand;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;
import com.learning.reelnet.modules.vocabulary.application.services.VocabularyApplicationService;
import com.learning.reelnet.modules.vocabulary.application.services.VocabularySetApplicationService;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;

import lombok.RequiredArgsConstructor;

@Component("CreateVocabularySetCommandHandler")
@RequiredArgsConstructor
public class CreateVocabularySetCommandHandler implements CommandHandler<UUID, CreateVocabularySetCommand> {
    final VocabularySetApplicationService vocabularySetApplicationService;
    final VocabularyApplicationService vocabularyApplicationService;

    @Override
    public UUID handle(CreateVocabularySetCommand command) throws Exception {
        // Create the vocabulary set
        VocabularySetDto vocabularySet = VocabularySetDto.builder()
                .name(command.getName())
                .description(command.getDescription())
                .visibility(
                        command.getVisibility() != null ? command.getVisibility() : VocabularySet.Visibility.PRIVATE)
                .category(command.getCategory())
                .createdBy(command.getCreatedBy())
                .build();

        // Save the vocabulary set
        VocabularySetDto savedSet = vocabularySetApplicationService.createVocabularySet(vocabularySet);

        // Add initial vocabularies if provided
        if (command.getVocabularyIds() != null && !command.getVocabularyIds().isEmpty()) {
            vocabularySetApplicationService.addVocabulariesToSet(savedSet.getId(), command.getVocabularyIds());
        }

        return savedSet.getId();
    }
}
