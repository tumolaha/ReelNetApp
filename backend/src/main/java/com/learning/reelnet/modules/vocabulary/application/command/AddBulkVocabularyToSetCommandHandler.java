package com.learning.reelnet.modules.vocabulary.application.command;

import com.learning.reelnet.common.application.cqrs.command.CommandHandler;
import com.learning.reelnet.modules.vocabulary.api.command.AddBulkVocabularyToSetCommand;
import com.learning.reelnet.modules.vocabulary.api.command.AddBulkVocabularyToSetCommand.VocabularyItem;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySetItem;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularySetRepository;

import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularyRepository;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularySetItemRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Component("addBulkVocabularyToSetCommandHandler")
@RequiredArgsConstructor
@Slf4j
public class AddBulkVocabularyToSetCommandHandler implements CommandHandler<Integer, AddBulkVocabularyToSetCommand> {

    private final VocabularySetRepository vocabularySetRepository;
    private final VocabularyRepository vocabularyRepository;
    private final VocabularySetItemRepository vocabularySetItemRepository;

    @Override
    @Transactional
    public Integer handle(AddBulkVocabularyToSetCommand command) throws Exception {
        // 1. Lấy vocabulary set từ ID
        VocabularySet vocabularySet = vocabularySetRepository.findById(command.getVocabularySetId());

        // 2. Tối ưu: Lấy tất cả vocabulary ID cần thêm
        Set<UUID> vocabularyIds = command.getVocabularyItems().stream()
                .map(commandItem -> commandItem.getVocabularyId())
                .filter(Objects::nonNull) // Lọc ra các ID không null
                .collect(Collectors.toSet());

        // 3. Tối ưu: Lấy tất cả vocabulary items hiện có để tránh trùng lặp
        Set<UUID> existingVocabIds = vocabularySetItemRepository
                .findByVocabularySetId(command.getVocabularySetId())
                .stream()
                .map(item -> item.getVocabulary().getId())
                .collect(Collectors.toSet());

        // 4. Tối ưu: Kiểm tra & loại bỏ các vocabulary đã tồn tại trong set
        vocabularyIds.removeAll(existingVocabIds);

        if (vocabularyIds.isEmpty()) {
            return 0; // Không có từ vựng mới để thêm
        }

        // 5. Tối ưu: Lấy tất cả vocabulary cần thêm trong một lần query
        // Map<UUID, Vocabulary> vocabularyMap = vocabularyRepository.findById(vocabularyIds)
        //         .stream()
        //         .collect(Collectors.toMap(Vocabulary::getId, v -> v));

        // 6. Tìm displayOrder lớn nhất hiện tại
        Integer maxOrder = vocabularySetItemRepository.findMaxDisplayOrderBySetId(command.getVocabularySetId());

        if (maxOrder == null) {
            maxOrder = 0;
        }
        // 7. Tạo batch items để insert
        List<VocabularySetItem> newItems = new ArrayList<>();
        Map<UUID, VocabularyItem> itemsMap = command.getVocabularyItems().stream()
                .filter(item -> !existingVocabIds.contains(item.getVocabularyId()))
                .collect(Collectors.toMap(VocabularyItem::getVocabularyId, item -> item));

        // int counter = 1;
        // for (UUID vocabId : vocabularyIds) {
        //     Vocabulary vocabulary = vocabularyMap.get(vocabId);
        //     if (vocabulary == null) {
        //         if (command.isFailOnError()) {
        //             throw new EntityNotFoundException("Vocabulary with id " + vocabId + " not found");
        //         } else {
        //             log.warn("Vocabulary with id {} not found, skipping", vocabId);
        //             continue;
        //         }
        //     }

        //     VocabularyItem itemData = itemsMap.get(vocabId);
        //     VocabularySetItem item = VocabularySetItem.builder()
        //             .vocabularySet(vocabularySet)
        //             .vocabulary(vocabulary)
        //             .displayOrder(itemData.getDisplayOrder() != null
        //                     ? itemData.getDisplayOrder()
        //                     : maxOrder + counter++)
        //             .customDefinition(itemData.getCustomDefinition())
        //             .customExample(itemData.getCustomExample())
        //             .build();

        //     newItems.add(item);
        // }

        // 8. Tối ưu: Lưu tất cả items trong một lần gọi
        vocabularySetItemRepository.saveAll(newItems);

        return newItems.size();
    }
}
