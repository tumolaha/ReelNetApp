package com.learning.reelnet.common.api.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchParams {
    private String query;

    @Builder.Default
    private List<String> fields = new ArrayList<>();

    public boolean hasSearch() {
        return query != null && !query.isEmpty() && !fields.isEmpty();
    }

    public void addField(String field) {
        fields.add(field);
    }
}
