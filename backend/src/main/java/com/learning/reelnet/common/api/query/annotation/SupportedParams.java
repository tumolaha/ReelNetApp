package com.learning.reelnet.common.api.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportedParams {
    String[] allowedSortFields() default {};
    String[] allowedFilterFields() default {};
    String[] allowedSearchFields() default {};
    int maxPageSize() default 100;
}
