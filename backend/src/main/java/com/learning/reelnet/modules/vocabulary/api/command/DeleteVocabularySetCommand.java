package com.learning.reelnet.modules.vocabulary.api.command;

import java.util.UUID;

import com.learning.reelnet.common.application.cqrs.command.Command;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DeleteVocabularySetCommand implements Command<Boolean> {
    private UUID id;
}

