package com.learning.reelnet.common.infrastructure.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to log method execution time.
 * Can be applied to methods to log how long they take to execute.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
    
    /**
     * The log level to use.
     * @return the log level
     */
    LogLevel value() default LogLevel.DEBUG;
    
    /**
     * Available log levels.
     */
    enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR
    }
}