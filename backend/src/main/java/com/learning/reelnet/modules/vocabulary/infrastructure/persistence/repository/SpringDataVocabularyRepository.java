package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.learning.reelnet.modules.vocabulary.domain.model.Vocabulary;

@Repository
public interface SpringDataVocabularyRepository extends JpaRepository<Vocabulary, UUID>, JpaSpecificationExecutor<Vocabulary> {
    // This interface extends other repositories to combine their functionalities.
    // No additional methods are needed here as it serves as a marker interface.
    
}
