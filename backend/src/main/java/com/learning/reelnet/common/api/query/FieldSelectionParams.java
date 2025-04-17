package com.learning.reelnet.common.api.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

/**
 * Lớp xử lý các tham số lựa chọn trường dữ liệu.
 * Hỗ trợ các tính năng như chọn, loại trừ và nhúng dữ liệu.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldSelectionParams {

    /**
     * Danh sách các trường cần trả về
     */
    private List<String> fields = new ArrayList<>();

    /**
     * Danh sách các trường cần loại trừ
     */
    private List<String> exclude = new ArrayList<>();

    /**
     * Danh sách các quan hệ cần bao gồm (eager loading)
     */
    private List<String> include = new ArrayList<>();

    /**
     * Map lưu trữ các tham số nhúng dữ liệu với các tùy chọn
     * Key: Tên quan hệ
     * Value: Map chứa các tùy chọn (limit, sort, filter, etc.)
     */
    private Map<String, Map<String, Object>> embed = new HashMap<>();

    /**
     * Thêm một trường vào danh sách cần trả về
     *
     * @param field Tên trường
     */
    public void addField(String field) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        fields.add(field);
    }

    /**
     * Thêm một trường vào danh sách loại trừ
     *
     * @param field Tên trường
     */
    public void addExclude(String field) {
        if (exclude == null) {
            exclude = new ArrayList<>();
        }
        exclude.add(field);
    }

    /**
     * Thêm một quan hệ vào danh sách cần bao gồm
     *
     * @param relation Tên quan hệ
     */
    public void addInclude(String relation) {
        if (include == null) {
            include = new ArrayList<>();
        }
        include.add(relation);
    }

    /**
     * Thêm một quan hệ nhúng với các tùy chọn
     *
     * @param relation Tên quan hệ
     * @param options Map chứa các tùy chọn
     */
    public void addEmbed(String relation, Map<String, Object> options) {
        if (embed == null) {
            embed = new HashMap<>();
        }
        embed.put(relation, options);
    }

    /**
     * Thêm một quan hệ nhúng với tùy chọn limit
     *
     * @param relation Tên quan hệ
     * @param limit Giới hạn số lượng bản ghi
     */
    public void addEmbedWithLimit(String relation, int limit) {
        Map<String, Object> options = new HashMap<>();
        options.put("limit", limit);
        addEmbed(relation, options);
    }

    /**
     * Thêm một quan hệ nhúng với tùy chọn sắp xếp
     *
     * @param relation Tên quan hệ
     * @param sort Trường sắp xếp
     * @param order Thứ tự sắp xếp (asc, desc)
     */
    public void addEmbedWithSort(String relation, String sort, String order) {
        Map<String, Object> options = new HashMap<>();
        options.put("sort", sort);
        options.put("order", order);
        addEmbed(relation, options);
    }

    /**
     * Kiểm tra xem có yêu cầu lựa chọn trường không
     *
     * @return true nếu có yêu cầu lựa chọn trường
     */
    public boolean hasFieldSelection() {
        return (fields != null && !fields.isEmpty());
    }

    /**
     * Kiểm tra xem có yêu cầu loại trừ trường không
     *
     * @return true nếu có yêu cầu loại trừ trường
     */
    public boolean hasExclusion() {
        return (exclude != null && !exclude.isEmpty());
    }

    /**
     * Kiểm tra xem có yêu cầu bao gồm quan hệ không
     *
     * @return true nếu có yêu cầu bao gồm quan hệ
     */
    public boolean hasIncludes() {
        return (include != null && !include.isEmpty());
    }

    /**
     * Kiểm tra xem có yêu cầu nhúng dữ liệu không
     *
     * @return true nếu có yêu cầu nhúng dữ liệu
     */
    public boolean hasEmbeds() {
        return (embed != null && !embed.isEmpty());
    }

    /**
     * Kiểm tra xem một trường có được chọn không
     *
     * @param field Tên trường
     * @return true nếu trường được chọn
     */
    public boolean isFieldSelected(String field) {
        if (!hasFieldSelection()) {
            return true;  // Nếu không có danh sách fields, mặc định là chọn tất cả
        }
        return fields.contains(field);
    }

    /**
     * Kiểm tra xem một trường có bị loại trừ không
     *
     * @param field Tên trường
     * @return true nếu trường bị loại trừ
     */
    public boolean isFieldExcluded(String field) {
        if (!hasExclusion()) {
            return false;  // Nếu không có danh sách exclude, mặc định là không loại trừ
        }
        return exclude.contains(field);
    }

    /**
     * Lấy tất cả các tham số lựa chọn dữ liệu
     *
     * @return Map với key là loại lựa chọn và value là danh sách các trường/quan hệ
     */
    public Map<String, Object> getAllSelectionParams() {
        Map<String, Object> selectionParams = new HashMap<>();
        if (hasFieldSelection()) {
            selectionParams.put("fields", fields);
        }
        if (hasExclusion()) {
            selectionParams.put("exclude", exclude);
        }
        if (hasIncludes()) {
            selectionParams.put("include", include);
        }
        if (hasEmbeds()) {
            selectionParams.put("embed", embed);
        }
        return selectionParams;
    }
} 