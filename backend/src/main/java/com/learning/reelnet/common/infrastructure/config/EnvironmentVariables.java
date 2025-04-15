package com.learning.reelnet.common.infrastructure.config;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Utility class to access environment variables in the application.
 * This provides a consistent way to access environment variables regardless of source.
 */
@Component
public class EnvironmentVariables {

    private final Environment environment;

    public EnvironmentVariables(Environment environment) {
        this.environment = environment;
    }

    /**
     * Get a required environment variable. Throws an exception if the variable is not present.
     *
     * @param key the environment variable key
     * @return the value of the environment variable
     * @throws IllegalStateException if the environment variable is not set
     */
    public String getRequired(String key) {
        String value = environment.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("Required environment variable '" + key + "' is not set");
        }
        return value;
    }

    /**
     * Get an environment variable with a default value if not present.
     *
     * @param key the environment variable key
     * @param defaultValue the default value to return if the variable is not set
     * @return the value of the environment variable or the default value
     */
    public String get(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    /**
     * Check if an environment variable is set.
     *
     * @param key the environment variable key
     * @return true if the environment variable is set, false otherwise
     */
    public boolean has(String key) {
        return environment.getProperty(key) != null;
    }

    /**
     * Get an environment variable as a boolean.
     *
     * @param key the environment variable key
     * @param defaultValue the default value to return if the variable is not set
     * @return the boolean value of the environment variable or the default value
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = environment.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * Get an environment variable as an integer.
     *
     * @param key the environment variable key
     * @param defaultValue the default value to return if the variable is not set
     * @return the integer value of the environment variable or the default value
     */
    public int getInt(String key, int defaultValue) {
        String value = environment.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Get an environment variable as a long.
     *
     * @param key the environment variable key
     * @param defaultValue the default value to return if the variable is not set
     * @return the long value of the environment variable or the default value
     */
    public long getLong(String key, long defaultValue) {
        String value = environment.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
} 