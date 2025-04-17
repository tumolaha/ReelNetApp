package com.learning.reelnet.common.api.query;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Cấu hình xử lý các tham số query trong ứng dụng.
 * Đăng ký các resolver để tự động chuyển đổi các tham số HTTP query thành đối
 * tượng Java.
 */
@Configuration
public class QueryParamConfig implements WebMvcConfigurer {

    /**
     * Kích thước trang mặc định
     */
    @Value("${api.pagination.default-page-size:20}")
    private int defaultPageSize;

    /**
     * Kích thước trang tối đa
     */
    @Value("${api.pagination.max-page-size:100}")
    private int maxPageSize;

    /**
     * Tên tham số số trang
     */
    @Value("${api.pagination.page-parameter:page}")
    private String pageParameter;

    /**
     * Tên tham số kích thước trang
     */
    @Value("${api.pagination.size-parameter:limit}")
    private String sizeParameter;

    /**
     * Tên tham số sắp xếp
     */
    @Value("${api.sorting.sort-parameter:sort}")
    private String sortParameter;

    /**
     * Đăng ký các ArgumentResolver để xử lý các tham số phương thức controller
     *
     * @param resolvers Danh sách ArgumentResolver cần đăng ký
     */
    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> resolvers) {
        // Cấu hình Pageable resolver
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        pageableResolver.setPageParameterName(pageParameter);
        pageableResolver.setSizeParameterName(sizeParameter);
        pageableResolver.setOneIndexedParameters(true); // Bắt đầu từ trang 1 thay vì 0
        pageableResolver.setMaxPageSize(maxPageSize);
        pageableResolver.setFallbackPageable(org.springframework.data.domain.PageRequest.of(0, defaultPageSize));
        resolvers.add(pageableResolver);

        // Đăng ký các resolver tùy chỉnh
        resolvers.add(new QueryParamsResolver());
        resolvers.add(new FilterParamsResolver());
        resolvers.add(new SearchParamsResolver());
        resolvers.add(new FieldSelectionParamsResolver());
    }

    /**
     * Resolver cho QueryParams
     * Được sử dụng để chuyển đổi các tham số query thành đối tượng QueryParams
     */
    public static class QueryParamsResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(@NonNull MethodParameter parameter) {
            return parameter.getParameterType().equals(QueryParams.class);
        }

        @Override
        public Object resolveArgument(@NonNull MethodParameter parameter,
                @Nullable ModelAndViewContainer mavContainer,
                @NonNull NativeWebRequest webRequest,
                @Nullable WebDataBinderFactory binderFactory) {

            // Tạo đối tượng QueryParams và điền thông tin từ request
            QueryParams queryParams = new QueryParams();

            // Cần triển khai chi tiết việc trích xuất các tham số từ request và điền vào
            // queryParams
            // ...

            return queryParams;
        }
    }

    /**
     * Resolver cho FilterParams
     * Được sử dụng để chuyển đổi các tham số query thành đối tượng FilterParams
     */
    public static class FilterParamsResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(@NonNull MethodParameter parameter) {
            return parameter.getParameterType().equals(FilterParams.class);
        }

        /**
         * Được sử dụng để chuyển đổi các tham số query thành đối tượng FilterParams
         */
        @Override
        public Object resolveArgument(@NonNull MethodParameter parameter,
                @Nullable ModelAndViewContainer mavContainer,
                @NonNull NativeWebRequest webRequest,
                @Nullable WebDataBinderFactory binderFactory) {

            // Tạo đối tượng FilterParams và điền thông tin từ request
            FilterParams filterParams = new FilterParams();

            // Cần triển khai chi tiết việc trích xuất các tham số từ request và điền vào
            // filterParams
            // ...

            return filterParams;
        }
    }

    /**
     * Resolver cho SearchParams
     * Được sử dụng để chuyển đổi các tham số query thành đối tượng SearchParams
     */
    public static class SearchParamsResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(@NonNull MethodParameter parameter) {
            return parameter.getParameterType().equals(SearchParams.class);
        }

        @Override
        public Object resolveArgument(@NonNull MethodParameter parameter,
                @Nullable ModelAndViewContainer mavContainer,
                @NonNull NativeWebRequest webRequest,
                @Nullable WebDataBinderFactory binderFactory) {

            // Tạo đối tượng SearchParams và điền thông tin từ request
            SearchParams searchParams = new SearchParams();

            // Cần triển khai chi tiết việc trích xuất các tham số từ request và điền vào
            // searchParams
            // ...

            return searchParams;
        }
    }

    /**
     * Resolver cho FieldSelectionParams
     * Được sử dụng để chuyển đổi các tham số query thành đối tượng
     * FieldSelectionParams
     */
    public static class FieldSelectionParamsResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(@NonNull MethodParameter parameter) {
            return parameter.getParameterType().equals(FieldSelectionParams.class);
        }

        @Override
        public Object resolveArgument(@NonNull MethodParameter parameter,
                @Nullable ModelAndViewContainer mavContainer,
                @NonNull NativeWebRequest webRequest,
                @Nullable WebDataBinderFactory binderFactory) {

            // Tạo đối tượng FieldSelectionParams và điền thông tin từ request
            FieldSelectionParams fieldSelectionParams = new FieldSelectionParams();

            // Cần triển khai chi tiết việc trích xuất các tham số từ request và điền vào
            // fieldSelectionParams
            // ...

            return fieldSelectionParams;
        }
    }
}