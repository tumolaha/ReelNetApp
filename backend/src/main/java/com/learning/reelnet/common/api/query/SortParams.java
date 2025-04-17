package com.learning.reelnet.common.api.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Lớp xử lý các tham số sắp xếp.
 * Có thể sử dụng độc lập hoặc kết hợp với QueryParams.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SortParams {

    /**
     * Danh sách các trường sắp xếp.
     * Định dạng: "field1,asc|field2,desc"
     * Hoặc: "field1,-field2" (dấu - đại diện cho sắp xếp giảm dần)
     */
    private String sort;

    /**
     * Chuyển đổi chuỗi sort thành đối tượng Sort của Spring
     *
     * @return Đối tượng Sort
     */
    public Sort toSort() {
        if (!StringUtils.hasText(sort)) {
            return Sort.unsorted();
        }

        List<Sort.Order> orders = new ArrayList<>();

        // Xử lý format: "field1,asc|field2,desc"
        if (sort.contains("|")) {
            String[] sortFields = sort.split("\\|");
            for (String field : sortFields) {
                if (field.contains(",")) {
                    String[] parts = field.split(",");
                    if (parts.length == 2) {
                        String fieldName = parts[0].trim();
                        String direction = parts[1].trim().toLowerCase();
                        if ("desc".equals(direction)) {
                            orders.add(Sort.Order.desc(fieldName));
                        } else {
                            orders.add(Sort.Order.asc(fieldName));
                        }
                    }
                } else {
                    orders.add(Sort.Order.asc(field.trim()));
                }
            }
        } 
        // Xử lý format: "field1,-field2"
        else {
            String[] sortFields = sort.split(",");
            for (String field : sortFields) {
                field = field.trim();
                if (field.startsWith("-")) {
                    orders.add(Sort.Order.desc(field.substring(1)));
                } else {
                    orders.add(Sort.Order.asc(field));
                }
            }
        }

        return Sort.by(orders);
    }

    /**
     * Tạo đối tượng Sort với các trường cho phép
     *
     * @param allowedFields Danh sách các trường được phép sắp xếp
     * @return Đối tượng Sort
     */
    public Sort toSort(List<String> allowedFields) {
        if (!StringUtils.hasText(sort) || allowedFields == null || allowedFields.isEmpty()) {
            return Sort.unsorted();
        }

        Sort originalSort = toSort();
        List<Sort.Order> filteredOrders = new ArrayList<>();

        for (Sort.Order order : originalSort) {
            if (allowedFields.contains(order.getProperty())) {
                filteredOrders.add(order);
            }
        }

        return filteredOrders.isEmpty() ? Sort.unsorted() : Sort.by(filteredOrders);
    }

    /**
     * Tạo đối tượng Sort với các trường cho phép
     *
     * @param allowedFields Mảng các trường được phép sắp xếp
     * @return Đối tượng Sort
     */
    public Sort toSort(String... allowedFields) {
        return toSort(Arrays.asList(allowedFields));
    }

    /**
     * Kiểm tra xem có tham số sắp xếp không
     *
     * @return true nếu có tham số sắp xếp
     */
    public boolean hasSorting() {
        return StringUtils.hasText(sort);
    }
} 