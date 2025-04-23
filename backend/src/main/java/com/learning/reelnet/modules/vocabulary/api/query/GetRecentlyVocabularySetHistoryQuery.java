package com.learning.reelnet.modules.vocabulary.api.query;

import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.common.api.query.SearchParams;
import com.learning.reelnet.common.application.cqrs.query.Query;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class GetRecentlyVocabularySetHistoryQuery implements Query<Page<VocabularySetDto>> {
    private final String userId;
    private final QueryParams queryParams;
    private final FilterParams filterParams;
    private final SearchParams searchParams;

}
