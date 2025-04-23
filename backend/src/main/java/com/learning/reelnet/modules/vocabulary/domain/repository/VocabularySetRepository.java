package com.learning.reelnet.modules.vocabulary.domain.repository;

import java.util.UUID;

import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.SearchParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularySetRepository {
   
    VocabularySet findById(UUID id);

    VocabularySet save(VocabularySet vocabularySet); 

    void deleteById(UUID id);  

    Page<VocabularySet> findAll(QueryParams queryParam, FilterParams filterParams, SearchParams searchParams); // Method
                                                                                                               
    Page<VocabularySet> findByUserId(String userId); 
        
    Page<VocabularySet> findRecentlyByUser(String userId, QueryParams queryParams, FilterParams filterParams, SearchParams searchParams);

    boolean existsById(UUID id);  
}
