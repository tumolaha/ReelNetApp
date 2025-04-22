package com.learning.reelnet.modules.vocabulary.api.query;

import org.springframework.data.domain.Page;

import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.common.api.query.SearchParams;
import com.learning.reelnet.common.application.cqrs.query.Query;
import com.learning.reelnet.modules.vocabulary.api.dto.VocabularySetDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetAllVocabularySetQuery implements Query<Page<VocabularySetDto>> {
    QueryParams queryParams;
    FilterParams filterParams;
    SearchParams searchParams;
}
