package com.learning.reelnet.modules.vocabulary.infrastructure.persistence.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.common.api.query.SearchParams;
// import com.learning.reelnet.common.api.query.annotation.QueryParam;
import com.learning.reelnet.modules.vocabulary.domain.model.VocabularySet;
import com.learning.reelnet.modules.vocabulary.domain.repository.VocabularySetRepository;



public class JpaVocabularySetRepositoryImpl implements VocabularySetRepository {
    // Implement the methods defined in the VocabularySetRepository interface here
    private final SpringDataVocabularySetRepository springDataRepository;
    
    public JpaVocabularySetRepositoryImpl(SpringDataVocabularySetRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }


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
    public List<VocabularySet> findAll(QueryParams queryParam, FilterParams filterParams, SearchParams searchParams) {
        // 1. Xây dựng Specification từ filterParams và searchParams
        Specification<VocabularySet> spec = createSpecification(filterParams, searchParams);
        
        // 2. Chuyển QueryParam thành Pageable
        Pageable pageable = queryParam.toPageable();
        
        // 3. Thực hiện truy vấn với Specification và Pageable
        if (pageable.isPaged()) {
            Page<VocabularySet> result = springDataRepository.findAll(spec, pageable);
            queryParam.updatePaginationInfo(result.getTotalElements());
            return result.getContent();
        } else {
            return springDataRepository.findAll(spec);
        }
    }
    /**
     * Tạo Specification từ FilterParams và SearchParams
     */
    private Specification<VocabularySet> createSpecification(FilterParams filterParams, SearchParams searchParams) {
        Specification<VocabularySet> spec = Specification.where(null);
        
        // Xử lý SearchParams
        if (searchParams != null && searchParams.hasSearch()) {
            String keyword = searchParams.getQuery();
            spec = spec.and((root, query, cb) -> {
                return cb.or(
                    cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%")
                );
            });
        }
        
        // Xử lý FilterParams
        if (filterParams != null && filterParams.hasFilters()) {
            // Lấy tất cả các filter được định nghĩa
            Map<String, Map<String, Object>> filters = filterParams.getFilters();
            
            for (Map.Entry<String, Map<String, Object>> entry : filters.entrySet()) {
                String field = entry.getKey();
                Map<String, Object> valueMap = entry.getValue();
                Object value = valueMap.get("value"); // Assuming the actual value is stored under a "value" key
                
                // Xử lý từng trường hợp filter
                switch (field) {
                    case "visibility":
                        try {
                            VocabularySet.Visibility visibility = VocabularySet.Visibility.valueOf(value.toString());
                            spec = spec.and((root, query, cb) -> cb.equal(root.get("visibility"), visibility));
                        } catch (IllegalArgumentException e) {
                            // Bỏ qua nếu giá trị không hợp lệ
                        }
                        break;
                        
                    case "category":
                        try {
                            VocabularySet.Category category = VocabularySet.Category.valueOf(value.toString());
                            spec = spec.and((root, query, cb) -> cb.equal(root.get("category"), category));
                        } catch (IllegalArgumentException e) {
                            // Bỏ qua nếu giá trị không hợp lệ
                        }
                        break;
                        
                    case "difficultyLevel":
                        try {
                            VocabularySet.DifficultyLevel difficulty = VocabularySet.DifficultyLevel.valueOf(value.toString());
                            spec = spec.and((root, query, cb) -> cb.equal(root.get("difficultyLevel"), difficulty));
                        } catch (IllegalArgumentException e) {
                            // Bỏ qua nếu giá trị không hợp lệ
                        }
                        break;
                        
                    case "createdBy":
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("createdBy"), value.toString()));
                        break;
                        
                    // Thêm các trường hợp khác khi cần
                }
            }
        }
        
        return spec;
    }

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
