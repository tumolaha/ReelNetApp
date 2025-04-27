package com.learning.reelnet.modules.vocabulary.api.command;

import com.learning.reelnet.common.application.cqrs.command.Command;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.Category;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.DifficultyLevel;
import com.learning.reelnet.modules.vocabulary.domain.valueobject.Visibility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVocabularySetCommand implements Command<UUID> {
    private String name;
    private String description;
    private Visibility visibility;
    private DifficultyLevel difficultyLevel;
    private Category category;
    private List<UUID> vocabularyIds;
    private String createdBy;

    
}