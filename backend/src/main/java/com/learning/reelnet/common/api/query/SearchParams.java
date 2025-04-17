package com.learning.reelnet.common.api.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lớp xử lý các tham số tìm kiếm.
 * Có thể sử dụng độc lập hoặc kết hợp với QueryParams.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchParams {

    /**
     * Từ khóa tìm kiếm tổng quát (q parameter)
     */
    private String query;

    /**
     * Tìm kiếm theo trường cụ thể
     * Key: tên trường, Value: giá trị tìm kiếm
     */
    private Map<String, String> fieldSearch = new HashMap<>();

    /**
     * Chế độ tìm kiếm
     * Các giá trị có thể: exact, fuzzy, semantic
     */
    private String searchMode = "fuzzy";

    /**
     * Toán tử tìm kiếm
     * Các giá trị có thể: and, or
     */
    private String searchOperator = "or";

    /**
     * Danh sách các trường cần tìm kiếm
     * Nếu rỗng, tìm kiếm trên tất cả các trường mặc định
     */
    private List<String> searchFields = new ArrayList<>();

    /**
     * Kiểm tra xem có tham số tìm kiếm không
     *
     * @return true nếu có tham số tìm kiếm
     */
    public boolean hasSearch() {
        return (query != null && !query.trim().isEmpty()) || 
               (fieldSearch != null && !fieldSearch.isEmpty());
    }

    /**
     * Kiểm tra xem có tìm kiếm theo trường cụ thể không
     *
     * @param fieldName Tên trường
     * @return true nếu có tìm kiếm theo trường cụ thể
     */
    public boolean hasFieldSearch(String fieldName) {
        return fieldSearch != null && fieldSearch.containsKey(fieldName);
    }

    /**
     * Lấy giá trị tìm kiếm theo trường cụ thể
     *
     * @param fieldName Tên trường
     * @return Giá trị tìm kiếm
     */
    public String getFieldSearchValue(String fieldName) {
        return fieldSearch != null ? fieldSearch.get(fieldName) : null;
    }

    /**
     * Thêm tìm kiếm theo trường cụ thể
     *
     * @param fieldName Tên trường
     * @param value Giá trị tìm kiếm
     */
    public void addFieldSearch(String fieldName, String value) {
        if (fieldSearch == null) {
            fieldSearch = new HashMap<>();
        }
        fieldSearch.put(fieldName, value);
    }

    /**
     * Xác định xem có sử dụng toán tử AND không
     *
     * @return true nếu toán tử tìm kiếm là AND
     */
    public boolean isAndOperator() {
        return "and".equalsIgnoreCase(searchOperator);
    }

    /**
     * Xác định xem có sử dụng tìm kiếm chính xác không
     *
     * @return true nếu chế độ tìm kiếm là exact
     */
    public boolean isExactMatch() {
        return "exact".equalsIgnoreCase(searchMode);
    }

    /**
     * Xác định xem có sử dụng tìm kiếm ngữ nghĩa không
     * 
     * @return true nếu chế độ tìm kiếm là semantic
     */
    public boolean isSemanticSearch() {
        return "semantic".equalsIgnoreCase(searchMode);
    }
} 