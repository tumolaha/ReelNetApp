package com.learning.reelnet.common.infrastructure.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark non-blocking methods.
 * Methods annotated with this will be executed asynchronously.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NonBlocking {
    
    /**
     * The name of the executor to use.
     * @return the executor name
     */
    String executor() default "taskExecutor";
}