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
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySetHistory;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularySetRepository;
import com.learning.reelnet.modules.vocabulary.infrastructure.persistence.data.SpringDataVocabularySetRepository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class JpaVocabularySetRepositoryImpl implements VocabularySetRepository {
    private final SpringDataVocabularySetRepository springDataRepository;

    /*
     * * find vocabulary set by id
     * 
     * * * @param id ID of the vocabulary set to find
     * * * @return VocabularySet The vocabulary set with the specified ID
     * 
     */
    @Override
    public VocabularySet findById(UUID id) {
        return springDataRepository.findById(id).orElse(null); // Implemented method to find by ID
    }

    /*
     * * save vocabulary set
     * 
     * * * @param vocabularySet VocabularySet object to save
     * * * @return VocabularySet The saved vocabulary set
     * * * @description This method saves a vocabulary set to the database.
     */
    @Override
    public VocabularySet save(VocabularySet vocabularySet) {
        return springDataRepository.save(vocabularySet);
    }

    /*
     * * delete vocabulary set by id
     * 
     * * @param id ID of the vocabulary set to delete
     * * @description This method deletes a vocabulary set by its ID.
     */
    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(id);
    }

    /*
     * * find all vocabulary sets
     * 
     * * @param queryParam Pagination parameters
     * * @param filterParams Filter criteria
     * * @param searchParams Search criteria
     * * @return Page<VocabularySet> List of vocabulary sets
     * * @description This method retrieves all vocabulary sets based on the given
     * parameters.
     */
    @Override
    public Page<VocabularySet> findAll(QueryParams queryParam, FilterParams filterParams, SearchParams searchParams) {
        Specification<VocabularySet> spec = SpecificationFactory.buildSpecification(filterParams, searchParams);
        Pageable pageable = queryParam.toPageable();
        if (pageable.isPaged()) {
            Page<VocabularySet> result = springDataRepository.findAll(spec, pageable);
            queryParam.updatePaginationInfo(result.getTotalElements());
            return result;
        } else {
            return new PageImpl<>(springDataRepository.findAll(spec), pageable, springDataRepository.count(spec));
        }
    }

    /**
     * * find by user id
     * 
     * * @param userId ID of the user
     * * @return Page<VocabularySet> List of vocabulary sets for the user
     * * @description This method retrieves all vocabulary sets associated with a
     */

    @Override
    public Page<VocabularySet> findByUserId(String userId) {
        // Tạo một Pageable mặc định để đảm bảo trả về tất cả kết quả
        Pageable pageable = Pageable.unpaged();
        return springDataRepository.findByUserId(userId, pageable); 
    }

    /*
     * * find all vocabulary set by user id
     * 
     * * @param userId ID of the user
     * * @param queryParams Pagination parameters
     * * @param filterParams Filter criteria
     * * @param searchParams Search criteria
     * * @return Page<VocabularySet> List of recent vocabulary sets for the user
     * * @description This method retrieves all vocabulary sets associated with a
     * user
     */
    @Override
    public Page<VocabularySet> findRecentlyByUser(String userId, QueryParams queryParams, FilterParams filterParams,
            SearchParams searchParams) {
        // Build specification từ filter và search params
        Specification<VocabularySet> spec = SpecificationFactory.buildSpecification(filterParams, searchParams);
        Pageable pageable = queryParams.toPageable();

        Page<VocabularySetHistory> result = springDataRepository.findRecentlyByUser(
                userId,
                spec,
                pageable);
        queryParams.updatePaginationInfo(result.getTotalElements());
        return result.map(history -> history.getVocabularySet());
    }

    /**
     * * exists by id
     * 
     * * @param id ID of the vocabulary set
     * * @return boolean true if the vocabulary set exists, false otherwise
     */
    @Override
    public boolean existsById(UUID id) {
        return springDataRepository.existsById(id);
    }

}
