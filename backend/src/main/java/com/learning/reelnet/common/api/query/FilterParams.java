package com.learning.reelnet.common.api.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterParams {
    @Builder.Default
    private Map<String, Map<String, Object>> filters = new HashMap<>();
    
    public boolean hasFilters() {
        return filters != null && !filters.isEmpty();
    }
    
    public void addFilter(String field, String operator, Object value) {
        Map<String, Object> filterData = new HashMap<>();
        filterData.put("operator", operator);
        filterData.put("value", value);
        
        filters.put(field, filterData);
    }
    
    public Map<String, Object> getFilter(String field) {
        return filters.get(field);
    }
}
