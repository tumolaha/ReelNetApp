package com.learning.reelnet.modules.vocabulary.application.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.learning.reelnet.modules.vocabulary.api.dto.VocabularyDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class VocabularyApplicationService {
    @Transactional
    public VocabularyDto getVocabularyById(UUID vocabularyId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVocabularyById'");
    }
    
}
