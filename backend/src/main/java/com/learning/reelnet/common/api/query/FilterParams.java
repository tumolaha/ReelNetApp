package com.learning.reelnet.common.api.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Lớp xử lý các tham số lọc phức tạp.
 * Hỗ trợ nhiều phép toán lọc như equals, not equals, greater than, less than, between, in, etc.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterParams {

    /**
     * Tham số lọc
     * Cấu trúc: Map<String, Map<String, Object>>
     * - Khóa ngoài: Tên trường
     * - Khóa trong: Phép toán (eq, ne, gt, lt, gte, lte, in, between, etc.)
     * - Giá trị: Giá trị lọc
     */
    private Map<String, Map<String, Object>> filters = new HashMap<>();

    /**
     * Các phép toán lọc hợp lệ
     */
    public enum FilterOperation {
        EQ("eq"),           // Equals
        NE("ne"),           // Not equals
        GT("gt"),           // Greater than
        LT("lt"),           // Less than
        GTE("gte"),         // Greater than or equals
        LTE("lte"),         // Less than or equals
        IN("in"),           // In a set of values
        NIN("nin"),         // Not in a set of values
        BETWEEN("between"), // Between two values
        LIKE("like"),       // Like pattern
        EXISTS("exists"),   // Field exists
        FROM("from"),       // From date/time
        TO("to"),           // To date/time
        DATE("date");       // Specific date

        private final String code;

        FilterOperation(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static FilterOperation fromString(String code) {
            for (FilterOperation operation : FilterOperation.values()) {
                if (operation.getCode().equalsIgnoreCase(code)) {
                    return operation;
                }
            }
            throw new IllegalArgumentException("Invalid filter operation: " + code);
        }
    }

    /**
     * Thêm một điều kiện lọc
     *
     * @param field Tên trường
     * @param operation Phép toán
     * @param value Giá trị lọc
     */
    public void addFilter(String field, FilterOperation operation, Object value) {
        if (!filters.containsKey(field)) {
            filters.put(field, new HashMap<>());
        }
        filters.get(field).put(operation.getCode(), value);
    }

    /**
     * Thêm một điều kiện lọc với phép toán dạng chuỗi
     *
     * @param field Tên trường
     * @param operation Phép toán dạng chuỗi
     * @param value Giá trị lọc
     */
    public void addFilter(String field, String operation, Object value) {
        FilterOperation filterOp = FilterOperation.fromString(operation);
        addFilter(field, filterOp, value);
    }

    /**
     * Thêm một điều kiện lọc bằng (equals)
     *
     * @param field Tên trường
     * @param value Giá trị lọc
     */
    public void addEqualsFilter(String field, Object value) {
        addFilter(field, FilterOperation.EQ, value);
    }

    /**
     * Thêm một điều kiện lọc trong khoảng (between)
     *
     * @param field Tên trường
     * @param min Giá trị nhỏ nhất
     * @param max Giá trị lớn nhất
     */
    public void addBetweenFilter(String field, Object min, Object max) {
        List<Object> values = Arrays.asList(min, max);
        addFilter(field, FilterOperation.BETWEEN, values);
    }

    /**
     * Thêm một điều kiện lọc trong tập giá trị (in)
     *
     * @param field Tên trường
     * @param values Danh sách giá trị
     */
    public void addInFilter(String field, List<?> values) {
        addFilter(field, FilterOperation.IN, values);
    }

    /**
     * Thêm một điều kiện lọc thời gian từ (from)
     *
     * @param field Tên trường
     * @param dateTime Thời gian bắt đầu
     */
    public void addFromDateTimeFilter(String field, LocalDateTime dateTime) {
        addFilter(field, FilterOperation.FROM, dateTime);
    }

    /**
     * Thêm một điều kiện lọc thời gian đến (to)
     *
     * @param field Tên trường
     * @param dateTime Thời gian kết thúc
     */
    public void addToDateTimeFilter(String field, LocalDateTime dateTime) {
        addFilter(field, FilterOperation.TO, dateTime);
    }

    /**
     * Lấy điều kiện lọc cho một trường
     *
     * @param field Tên trường
     * @return Map các điều kiện lọc cho trường
     */
    public Map<String, Object> getFieldFilters(String field) {
        return filters.getOrDefault(field, Collections.emptyMap());
    }

    /**
     * Lấy giá trị lọc cho một trường và phép toán
     *
     * @param field Tên trường
     * @param operation Phép toán
     * @return Giá trị lọc
     */
    public Object getFilterValue(String field, FilterOperation operation) {
        Map<String, Object> fieldFilters = getFieldFilters(field);
        return fieldFilters.get(operation.getCode());
    }

    /**
     * Lấy giá trị lọc cho một trường và phép toán dạng chuỗi
     *
     * @param field Tên trường
     * @param operation Phép toán dạng chuỗi
     * @return Giá trị lọc
     */
    public Object getFilterValue(String field, String operation) {
        Map<String, Object> fieldFilters = getFieldFilters(field);
        return fieldFilters.get(operation);
    }

    /**
     * Kiểm tra xem có điều kiện lọc không
     *
     * @return true nếu có điều kiện lọc
     */
    public boolean hasFilters() {
        return filters != null && !filters.isEmpty();
    }

    /**
     * Kiểm tra xem có điều kiện lọc cho một trường không
     *
     * @param field Tên trường
     * @return true nếu có điều kiện lọc cho trường
     */
    public boolean hasFieldFilter(String field) {
        return filters != null && filters.containsKey(field) && !filters.get(field).isEmpty();
    }

    /**
     * Chuyển đổi chuỗi ngày tháng thành LocalDateTime
     *
     * @param dateTimeStr Chuỗi ngày tháng (ISO 8601)
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * Chuyển đổi chuỗi ngày thành LocalDate
     *
     * @param dateStr Chuỗi ngày (YYYY-MM-DD)
     * @return LocalDate
     */
    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
    }

    /**
     * Lấy danh sách các trường lọc
     *
     * @return Danh sách tên các trường lọc
     */
    public List<String> getFilterFields() {
        return new ArrayList<>(filters.keySet());
    }
} 