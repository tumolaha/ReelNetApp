package com.learning.reelnet.modules.vocabulary.domain.repository;

import java.util.List;
import java.util.UUID;

import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.SearchParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;

public interface VocabularySetRepository {
    // Contains more complex query methods
    List<VocabularySet> findByCriteria(String criteria); // Method signature only, implementation should be in the

    VocabularySet findById(UUID id);

    VocabularySet save(VocabularySet vocabularySet); // Method signature only, implementation should be in the service

    void deleteById(UUID id); // Method signature only, implementation should be in the service layer

    List<VocabularySet> findAll(QueryParams queryParam, FilterParams filterParams, SearchParams searchParams); // Method
                                                                                                               // signature
                                                                                                               // //
                                                                                                               // layer

    List<VocabularySet> findByUserId(String userId); // Method signature only, implementation should be in the service
                                                     // layer

    List<VocabularySet> findByCategory(VocabularySet.Category category); // Method signature only, implementation should
                                                                         // be in the service layer

}
