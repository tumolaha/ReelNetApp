package com.learning.reelnet.common.api.query.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.common.api.query.SearchParams;
import jakarta.persistence.criteria.JoinType;
import java.io.Serializable;

import org.springframework.data.jpa.domain.Specification;

@NoRepositoryBean
public interface QuerySpecificationRepository<T, ID extends Serializable> 
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    
    default Page<T> findWithSpecification(QueryParams queryParams, FilterParams filterParams, SearchParams searchParams) {
        return findAll(
            SpecificationFactory.buildSpecification(filterParams, searchParams),
            queryParams.toPageable()
        );
    }
    
    default long countWithSpecification(FilterParams filterParams, SearchParams searchParams) {
        return count(SpecificationFactory.buildSpecification(filterParams, searchParams));
    }
    
    default Page<T> findWithSpecificationAndFetch(
            QueryParams queryParams, 
            FilterParams filterParams, 
            SearchParams searchParams,
            String... fetchRelations) {
        
        Specification<T> mainSpec = SpecificationFactory.buildSpecification(filterParams, searchParams);
        
        Specification<T> fetchSpec = (root, query, cb) -> {
            if (Long.class != query.getResultType()) {
                for (String relation : fetchRelations) {
                    root.fetch(relation, JoinType.LEFT);
                }
            }
            return cb.conjunction();
        };
        
        return findAll(Specification.where(mainSpec).and(fetchSpec), 
                       queryParams.toPageable());
    }
}
