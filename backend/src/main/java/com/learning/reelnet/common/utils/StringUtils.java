package com.learning.reelnet.common.utils;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class for string manipulation operations.
 * This class provides methods for common string operations like checking if
 * a string is empty, generating random strings, and slug generation.
 */
public final class StringUtils {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGE_DASHES = Pattern.compile("(^-|-$)");

    // Private constructor to prevent instantiation
    private StringUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Checks if a string is null or empty.
     *
     * @param str the string to check
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Checks if a string is not null and not empty.
     *
     * @param str the string to check
     * @return true if the string is not null and not empty, false otherwise
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Returns the string if it is not null, or an empty string if it is null.
     *
     * @param str the string to check
     * @return the string if not null, an empty string otherwise
     */
    public static String defaultIfNull(String str) {
        return str == null ? "" : str;
    }

    /**
     * Returns the string if it is not null and not empty, or a default value if it is.
     *
     * @param str the string to check
     * @param defaultValue the default value to return if the string is null or empty
     * @return the string if not null and not empty, the default value otherwise
     */
    public static String defaultIfEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }

    /**
     * Generates a random alphanumeric string of the specified length.
     *
     * @param length the length of the string to generate
     * @return the generated random string
     */
    public static String randomAlphanumeric(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Length must be positive");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHA_NUMERIC.charAt(RANDOM.nextInt(ALPHA_NUMERIC.length())));
        }
        return sb.toString();
    }

    /**
     * Generates a random UUID as a string.
     *
     * @return the generated UUID string
     */
    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * Truncates a string to the specified length, adding an ellipsis if truncated.
     *
     * @param str the string to truncate
     * @param maxLength the maximum length of the string
     * @return the truncated string
     */
    public static String truncate(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }

    /**
     * Converts a string to a URL-friendly slug.
     *
     * @param input the string to convert
     * @return the generated slug
     */
    public static String toSlug(String input) {
        if (input == null) {
            return "";
        }

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutAccents = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}", "");
        String lowercase = withoutAccents.toLowerCase();
        String noWhitespace = WHITESPACE.matcher(lowercase).replaceAll("-");
        String noNonLatin = NON_LATIN.matcher(noWhitespace).replaceAll("");
        String noEdgeDashes = EDGE_DASHES.matcher(noNonLatin).replaceAll("");
        
        return noEdgeDashes;
    }

    /**
     * Joins a list of strings with the specified delimiter.
     *
     * @param list the list of strings to join
     * @param delimiter the delimiter to use
     * @return the joined string
     */
    public static String join(List<String> list, String delimiter) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.stream()
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.joining(delimiter));
    }

    /**
     * Splits a string into a list of strings using the specified delimiter.
     *
     * @param str the string to split
     * @param delimiter the delimiter to use
     * @return the list of strings
     */
    public static List<String> split(String str, String delimiter) {
        if (isEmpty(str)) {
            return List.of();
        }
        return Arrays.stream(str.split(delimiter))
                .map(String::trim)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());
    }

    /**
     * Capitalizes the first letter of each word in a string.
     *
     * @param str the string to capitalize
     * @return the capitalized string
     */
    public static String capitalizeWords(String str) {
        if (isEmpty(str)) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : str.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                result.append(c);
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }

    /**
     * Masks a part of a string with asterisks, useful for hiding sensitive information.
     *
     * @param str the string to mask
     * @param start the start index (inclusive)
     * @param end the end index (exclusive)
     * @return the masked string
     */
    public static String mask(String str, int start, int end) {
        if (isEmpty(str)) {
            return str;
        }

        if (start < 0) {
            start = 0;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start >= end) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        for (int i = start; i < end; i++) {
            sb.setCharAt(i, '*');
        }
        return sb.toString();
    }

    /**
     * Extracts the domain name from an email address.
     *
     * @param email the email address
     * @return the domain name, or null if the email is invalid
     */
    public static String extractDomainFromEmail(String email) {
        if (isEmpty(email) || !email.contains("@")) {
            return null;
        }
        return email.substring(email.lastIndexOf('@') + 1);
    }
}