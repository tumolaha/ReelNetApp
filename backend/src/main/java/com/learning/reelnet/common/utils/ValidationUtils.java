package com.learning.reelnet.common.utils;

import java.util.regex.Pattern;

/**
 * Utility class for validation operations.
 * This class provides methods for validating common inputs like emails,
 * passwords, and phone numbers.
 */
public final class ValidationUtils {

    // Common validation patterns
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PASSWORD_PATTERN = 
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");
    
    private static final Pattern PHONE_PATTERN = 
            Pattern.compile("^\\+?[0-9]{10,15}$");
    
    private static final Pattern USERNAME_PATTERN = 
            Pattern.compile("^[a-zA-Z0-9_-]{3,20}$");
    
    private static final Pattern ZIP_CODE_PATTERN = 
            Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$");
    
    private static final Pattern URL_PATTERN = 
            Pattern.compile("^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$");

    // Private constructor to prevent instantiation
    private ValidationUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Validates an email address.
     *
     * @param email the email address to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates a password.
     * A valid password must:
     * - Be at least 8 characters long
     * - Contain at least one digit
     * - Contain at least one lowercase letter
     * - Contain at least one uppercase letter
     * - Contain at least one special character (@#$%^&+=!)
     * - Not contain whitespace
     *
     * @param password the password to validate
     * @return true if the password is valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Validates a phone number.
     * A valid phone number must:
     * - Contain 10-15 digits
     * - Optionally start with a plus sign
     *
     * @param phoneNumber the phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    /**
     * Validates a username.
     * A valid username must:
     * - Be 3-20 characters long
     * - Contain only alphanumeric characters, underscores, and hyphens
     *
     * @param username the username to validate
     * @return true if the username is valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * Validates a US zip code.
     * A valid zip code must be in format: 12345 or 12345-6789
     *
     * @param zipCode the zip code to validate
     * @return true if the zip code is valid, false otherwise
     */
    public static boolean isValidZipCode(String zipCode) {
        if (StringUtils.isEmpty(zipCode)) {
            return false;
        }
        return ZIP_CODE_PATTERN.matcher(zipCode).matches();
    }

    /**
     * Validates a URL.
     *
     * @param url the URL to validate
     * @return true if the URL is valid, false otherwise
     */
    public static boolean isValidUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return false;
        }
        return URL_PATTERN.matcher(url).matches();
    }

    /**
     * Validates that a string is not empty and has a minimum length.
     *
     * @param str the string to validate
     * @param minLength the minimum required length
     * @return true if the string is valid, false otherwise
     */
    public static boolean hasMinLength(String str, int minLength) {
        return !StringUtils.isEmpty(str) && str.length() >= minLength;
    }

    /**
     * Validates that a string does not exceed a maximum length.
     *
     * @param str the string to validate
     * @param maxLength the maximum allowed length
     * @return true if the string is valid, false otherwise
     */
    public static boolean hasMaxLength(String str, int maxLength) {
        return str == null || str.length() <= maxLength;
    }

    /**
     * Validates that a string is within a length range.
     *
     * @param str the string to validate
     * @param minLength the minimum required length
     * @param maxLength the maximum allowed length
     * @return true if the string is valid, false otherwise
     */
    public static boolean isLengthBetween(String str, int minLength, int maxLength) {
        return !StringUtils.isEmpty(str) && str.length() >= minLength && str.length() <= maxLength;
    }

    /**
     * Validates that a number is within a range.
     *
     * @param value the value to validate
     * @param min the minimum allowed value
     * @param max the maximum allowed value
     * @return true if the value is valid, false otherwise
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Validates that a number is within a range.
     *
     * @param value the value to validate
     * @param min the minimum allowed value
     * @param max the maximum allowed value
     * @return true if the value is valid, false otherwise
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     * Validates that a string contains only alphabetic characters.
     *
     * @param str the string to validate
     * @return true if the string is valid, false otherwise
     */
    public static boolean isAlpha(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return str.chars().allMatch(Character::isLetter);
    }

    /**
     * Validates that a string contains only alphabetic characters and spaces.
     *
     * @param str the string to validate
     * @return true if the string is valid, false otherwise
     */
    public static boolean isAlphaSpace(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return str.chars().allMatch(c -> Character.isLetter(c) || Character.isWhitespace(c));
    }

    /**
     * Validates that a string contains only alphanumeric characters.
     *
     * @param str the string to validate
     * @return true if the string is valid, false otherwise
     */
    public static boolean isAlphanumeric(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return str.chars().allMatch(Character::isLetterOrDigit);
    }

    /**
     * Validates that a string contains only numeric characters.
     *
     * @param str the string to validate
     * @return true if the string is valid, false otherwise
     */
    public static boolean isNumeric(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return str.chars().allMatch(Character::isDigit);
    }
}