package com.learning.reelnet.modules.vocabulary.api.command;

import com.learning.reelnet.common.application.cqrs.command.Command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddBulkVocabularyToSetCommand implements Command<Integer> {
    private UUID vocabularySetId;
    private List<VocabularyItem> vocabularyItems;
    private boolean failOnError; // Có dừng lại nếu gặp lỗi hay không

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VocabularyItem {
        private UUID vocabularyId;
        private Integer displayOrder; // Tùy chọn
        private String customDefinition; // Tùy chọn
        private String customExample; // Tùy chọn

    }

    private String notes; // Tùy chọn private String notes; // Tùy chọn
}
