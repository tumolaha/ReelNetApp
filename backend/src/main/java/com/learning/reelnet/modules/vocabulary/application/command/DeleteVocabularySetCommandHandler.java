package com.learning.reelnet.modules.vocabulary.application.command;


import org.springframework.stereotype.Component;

import com.learning.reelnet.common.application.cqrs.command.CommandHandler;
import com.learning.reelnet.modules.vocabulary.api.command.DeleteVocabularySetCommand;
import com.learning.reelnet.modules.vocabulary.application.services.VocabularySetApplicationService;

import lombok.RequiredArgsConstructor;

@Component("DeleteVocabularySetCommandHandler")
@RequiredArgsConstructor
public class DeleteVocabularySetCommandHandler implements CommandHandler<Boolean, DeleteVocabularySetCommand> {
    private final VocabularySetApplicationService vocabularySetApplicationService;


    @Override
    public Boolean handle(DeleteVocabularySetCommand command) {
        vocabularySetApplicationService.deleteVocabularySet(command.getId());
        return true;
    }

}
