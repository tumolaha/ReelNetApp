package com.learning.reelnet.common.infrastructure.services;

import com.learning.reelnet.common.application.dto.PageResponse;
import com.learning.reelnet.common.application.dto.SearchCriteria;
import com.learning.reelnet.common.application.dto.SearchRequest;
import com.learning.reelnet.common.domain.base.BaseEntity;
import com.learning.reelnet.common.domain.base.BaseRepository;
import com.learning.reelnet.common.infrastructure.persistence.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Generic service for searching entities using specifications.
 *
 * @param <T>  the entity type
 * @param <ID> the entity ID type
 * @param <R>  the repository type
 */
@Service
@RequiredArgsConstructor
public class SearchService<T extends BaseEntity<ID>, ID extends Serializable, R extends BaseRepository<T, ID>> {

    private final R repository;
    private String[] searchableFields = {};

    /**
     * Set searchable fields for the _all field criterion.
     *
     * @param fields the fields to search in
     * @return this service
     */
    public SearchService<T, ID, R> withSearchableFields(String... fields) {
        this.searchableFields = fields;
        return this;
    }

    /**
     * Search for entities based on search criteria.
     *
     * @param searchRequest the search request
     * @return a page response with search results
     */
    public PageResponse<T> search(SearchRequest searchRequest) {
        return search(searchRequest, entity -> entity);
    }

    /**
     * Search for entities and map results to DTOs.
     *
     * @param searchRequest the search request
     * @param mapper        function to map entities to DTOs
     * @param <D>           the DTO type
     * @return a page response with search results
     */
    public <D> PageResponse<D> search(SearchRequest searchRequest, Function<T, D> mapper) {
        // Build the specification
        Specification<T> spec = buildSpecification(searchRequest);
        
        // Execute search with pagination and sorting
        Page<T> page = repository.findAll(spec, searchRequest.getPagination().toSpringPageRequest());
        
        // Map results to DTOs
        List<D> content = page.getContent().stream()
                .map(mapper)
                .toList();
        
        // Create the response
        return PageResponse.of(
                content,
                page.getTotalElements(),
                searchRequest.getPagination()
        );
    }

    /**
     * Build a specification from a search request.
     *
     * @param searchRequest the search request
     * @return the built specification
     */
    private Specification<T> buildSpecification(SearchRequest searchRequest) {
        SpecificationBuilder<T> builder = new SpecificationBuilder<T>()
                .withSearchableFields(searchableFields);
        
        // Add criteria from the search request
        for (SearchCriteria criterion : searchRequest.getCriteria()) {
            builder.with(criterion);
        }
        
        // Add a criterion for free text search
        if (searchRequest.getQuery() != null && !searchRequest.getQuery().isEmpty()) {
            builder.with(new SearchCriteria("_all", SearchCriteria.SearchOperator.LIKE, searchRequest.getQuery()));
        }
        
        // Add criteria from filters
        for (Map.Entry<String, Object> filter : searchRequest.getFilters().entrySet()) {
            builder.with(new SearchCriteria(filter.getKey(), SearchCriteria.SearchOperator.EQUALS, filter.getValue()));
        }
        
        return builder.build();
    }

    /**
     * Count entities matching the specification.
     *
     * @param searchRequest the search request
     * @return the count of matching entities
     */
    public long count(SearchRequest searchRequest) {
        Specification<T> spec = buildSpecification(searchRequest);
        return repository.count(spec);
    }

    /**
     * Find all entities matching the specification.
     *
     * @param searchRequest the search request
     * @return list of matching entities
     */
    public List<T> findAll(SearchRequest searchRequest) {
        Specification<T> spec = buildSpecification(searchRequest);
        return repository.findAll(spec);
    }
}