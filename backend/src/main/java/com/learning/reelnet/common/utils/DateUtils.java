package com.learning.reelnet.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Utility class for handling date and time operations.
 * This class provides methods for common date operations like formatting, parsing,
 * and calculating time differences.
 */
public final class DateUtils {

    // Standard formatters
    public static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ISO_DATE;
    public static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    public static final DateTimeFormatter STANDARD_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter STANDARD_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter HUMAN_READABLE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    // Private constructor to prevent instantiation
    private DateUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Converts java.util.Date to java.time.LocalDate.
     *
     * @param date the Date to convert
     * @return the LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * Converts java.util.Date to java.time.LocalDateTime.
     *
     * @param date the Date to convert
     * @return the LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * Converts java.time.LocalDate to java.util.Date.
     *
     * @param localDate the LocalDate to convert
     * @return the Date
     */
    public static Date fromLocalDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    /**
     * Converts java.time.LocalDateTime to java.util.Date.
     *
     * @param localDateTime the LocalDateTime to convert
     * @return the Date
     */
    public static Date fromLocalDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    /**
     * Formats a LocalDate using the specified formatter.
     *
     * @param date the date to format
     * @param formatter the formatter to use
     * @return the formatted date string
     */
    public static String format(LocalDate date, DateTimeFormatter formatter) {
        if (date == null) {
            return null;
        }
        return date.format(formatter);
    }

    /**
     * Formats a LocalDateTime using the specified formatter.
     *
     * @param dateTime the date-time to format
     * @param formatter the formatter to use
     * @return the formatted date-time string
     */
    public static String format(LocalDateTime dateTime, DateTimeFormatter formatter) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(formatter);
    }

    /**
     * Parses a date string using the specified formatter.
     *
     * @param dateString the date string to parse
     * @param formatter the formatter to use
     * @return the parsed LocalDate
     * @throws DateTimeParseException if the text cannot be parsed
     */
    public static LocalDate parseDate(String dateString, DateTimeFormatter formatter) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString, formatter);
    }

    /**
     * Parses a date-time string using the specified formatter.
     *
     * @param dateTimeString the date-time string to parse
     * @param formatter the formatter to use
     * @return the parsed LocalDateTime
     * @throws DateTimeParseException if the text cannot be parsed
     */
    public static LocalDateTime parseDateTime(String dateTimeString, DateTimeFormatter formatter) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    /**
     * Calculates the number of days between two dates.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return the number of days between the dates
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Calculates the number of hours between two date-times.
     *
     * @param startDateTime the start date-time
     * @param endDateTime the end date-time
     * @return the number of hours between the date-times
     */
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    /**
     * Checks if a date is in the past.
     *
     * @param date the date to check
     * @return true if the date is in the past, false otherwise
     */
    public static boolean isInPast(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    /**
     * Checks if a date-time is in the past.
     *
     * @param dateTime the date-time to check
     * @return true if the date-time is in the past, false otherwise
     */
    public static boolean isInPast(LocalDateTime dateTime) {
        return dateTime.isBefore(LocalDateTime.now());
    }

    /**
     * Returns a human-readable relative time string (e.g., "2 hours ago", "in 3 days").
     *
     * @param dateTime the date-time to convert
     * @return the human-readable relative time string
     */
    public static String toRelativeTimeString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        long hours = ChronoUnit.HOURS.between(dateTime, now);
        long days = ChronoUnit.DAYS.between(dateTime, now);

        if (minutes < 0) {
            // Future
            minutes = Math.abs(minutes);
            hours = Math.abs(hours);
            days = Math.abs(days);

            if (minutes < 60) return "in " + minutes + " minute" + (minutes != 1 ? "s" : "");
            if (hours < 24) return "in " + hours + " hour" + (hours != 1 ? "s" : "");
            if (days < 30) return "in " + days + " day" + (days != 1 ? "s" : "");
            return "in " + (days / 30) + " month" + (days / 30 != 1 ? "s" : "");
        } else {
            // Past
            if (minutes < 60) return minutes + " minute" + (minutes != 1 ? "s" : "") + " ago";
            if (hours < 24) return hours + " hour" + (hours != 1 ? "s" : "") + " ago";
            if (days < 30) return days + " day" + (days != 1 ? "s" : "") + " ago";
            return (days / 30) + " month" + (days / 30 != 1 ? "s" : "") + " ago";
        }
    }
}