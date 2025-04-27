package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.data;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.entity.VocabularyEntity;

@Repository
public interface SpringDataVocabularyRepository extends JpaRepository<VocabularyEntity, UUID> {

    Optional<VocabularyEntity> findByHeadword(String headword);
    
    List<VocabularyEntity> findByHeadwordContainingIgnoreCase(String headword);
    
    @Query("SELECT v FROM VocabularyEntity v WHERE LOWER(v.headword) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<VocabularyEntity> searchByKeyword(@Param("keyword") String keyword);
    
    List<VocabularyEntity> findByPosOrderByHeadwordAsc(String pos);
    
    List<VocabularyEntity> findByIsSystemTrue();
}
