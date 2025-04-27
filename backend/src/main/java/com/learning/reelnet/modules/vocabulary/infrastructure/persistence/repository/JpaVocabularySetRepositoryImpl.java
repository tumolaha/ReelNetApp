package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.common.api.query.SearchParams;
import com.learning.reelnet.common.api.query.utils.SpecificationFactory;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularySetRepository;
import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.data.SpringDataVocabularySetRepository;
import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.entity.VocabularySetEntity;
import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.mapper.VocabularySetEntityMapper;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class JpaVocabularySetRepositoryImpl implements VocabularySetRepository {
    private final SpringDataVocabularySetRepository springDataRepository;
    private final VocabularySetEntityMapper mapper;

    @Override
    public VocabularySet findById(UUID id) {
        return mapper.toDomainModel(springDataRepository.findById(id).orElse(null));
    }

    @Override
    public VocabularySet save(VocabularySet vocabularySet) {
        return mapper.toDomainModel(springDataRepository.save(mapper.toEntity(vocabularySet)));
    }

    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public Page<VocabularySet> findAll(QueryParams queryParam, FilterParams filterParams, SearchParams searchParams) {
        Specification<VocabularySetEntity> spec = SpecificationFactory.buildSpecification(filterParams, searchParams);
        Pageable pageable = queryParam.toPageable();
        if (pageable.isPaged()) {
            Page<VocabularySetEntity> result = springDataRepository.findAll(spec, pageable);
            queryParam.updatePaginationInfo(result.getTotalElements());
            return result.map(mapper::toDomainModel);
        } else {
            return new PageImpl<>(mapper.toDomainModels(springDataRepository.findAll(spec)), pageable,
                    springDataRepository.count(spec));
        }
    }

    @Override
    public Page<VocabularySet> findByUserId(String userId) {
        Pageable pageable = Pageable.unpaged();
        Page<VocabularySetEntity> entityPage = springDataRepository.findByUserId(userId, pageable);
        return entityPage.map(mapper::toDomainModel);
    }

    @Override
    public Page<VocabularySet> findRecentlyByUser(String userId, QueryParams queryParams, FilterParams filterParams,
            SearchParams searchParams) {
        // Use unchecked cast to handle specification type conversion
        @SuppressWarnings("unchecked")
        Specification<VocabularySetEntity> spec = (Specification<VocabularySetEntity>)(Specification<?>)
            SpecificationFactory.buildSpecification(filterParams, searchParams);
        Pageable pageable = queryParams.toPageable();

        Page<VocabularySetEntity> entityResult = springDataRepository.findRecentlyByUser(
                userId,
                spec,
                pageable);
        queryParams.updatePaginationInfo(entityResult.getTotalElements());
        return entityResult.map(mapper::toDomainModel);
    }

    @Override
    public boolean existsById(UUID id) {
        return springDataRepository.existsById(id);
    }
}
