package com.learning.reelnet.modules.vocabulary.api.command;

import java.util.List;
import java.util.UUID;

import com.learning.reelnet.common.application.cqrs.command.Command;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateVocabularySetCommand implements Command<Boolean> {   
    private UUID id;
    private String name;
    private String description;
    private VocabularySet.Visibility visibility;
    private VocabularySet.DifficultyLevel difficultyLevel;
    private VocabularySet.Category category;
    private List<UUID> vocabularyIds;
}
