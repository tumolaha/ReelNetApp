package com.learning.reelnet.modules.vocabulary.api.command;

import java.util.List;
import java.util.UUID;

import com.learning.reelnet.common.application.cqrs.command.Command;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.Category;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.DifficultyLevel;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.Visibility;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateVocabularySetCommand implements Command<Boolean> {   
    private UUID id;
    private String name;
    private String description;
    private Visibility visibility;
    private DifficultyLevel difficultyLevel;
    private Category category;
    private List<UUID> vocabularyIds;
}
