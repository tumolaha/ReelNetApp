package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.repository;

import java.util.List;
import java.util.Map;
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
// import com.learning.reelnet.common.api.query.annotation.QueryParam;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularySetRepository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class JpaVocabularySetRepositoryImpl implements VocabularySetRepository {
    // Implement the methods defined in the VocabularySetRepository interface here
    private final SpringDataVocabularySetRepository springDataRepository;

    @Override
    public List<VocabularySet> findByCriteria(String criteria) {
        return springDataRepository.findByCriteria(criteria);
    }

    /*
     * * Tìm kiếm theo ID của người dùng
     */
    @Override
    public VocabularySet findById(UUID id) {
        return springDataRepository.findById(id).orElse(null); // Implemented method to find by ID
    }

    /*
     * * ?Tìm kiếm theo độ khó
     */
    @Override
    public VocabularySet save(VocabularySet vocabularySet) {
        return springDataRepository.save(vocabularySet); // Implemented method to save vocabulary set
    }

    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(id); // Implemented method to delete by ID
    }

    @Override
    public Page<VocabularySet> findAll(QueryParams queryParam, FilterParams filterParams, SearchParams searchParams) {
        // 1. Xây dựng Specification từ filterParams và searchParams
        Specification<VocabularySet> spec = SpecificationFactory.buildSpecification(filterParams, searchParams);

        // 2. Chuyển QueryParam thành Pageable
        Pageable pageable = queryParam.toPageable();

        // 3. Thực hiện truy vấn với Specification và Pageable
        if (pageable.isPaged()) {
            Page<VocabularySet> result = springDataRepository.findAll(spec, pageable);
            queryParam.updatePaginationInfo(result.getTotalElements());
            return result;
        } else {
            return new PageImpl<>(springDataRepository.findAll(spec), pageable, springDataRepository.count(spec));
        }
    }

    /**
     * Tạo Specification từ FilterParams và SearchParams
     */

    @Override
    public List<VocabularySet> findByUserId(String userId) {
        return springDataRepository.findByUserId(userId); // Implemented method to find by user ID
    }

    @Override
    public List<VocabularySet> findByCategory(VocabularySet.Category category) {
        return springDataRepository.findByCategory(category); // Implemented method to find by category
    }

    // create vocabulary set

}
