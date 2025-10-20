package parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import exception.NeruneruneException;

/**
 * Provides utility methods to parse and format date-time strings
 * used in the Nerunerune application.
 * Handles conversion between user input, storage format,
 * and display format for date and time.
 */
public class DateTimeParser {

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a");
    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Parses a date-time string from user input.
     * Accepts strings either with just a date (dd-MM-yyyy) or with date and time (dd-MM-yyyy HHmm).
     * Returns a LocalDateTime object with time set to start of day if only date is given (dd-MM-yyyy 0000).
     *
     * @param dateTimeString the input date-time string
     * @return LocalDateTime parsed from the input string
     * @throws NeruneruneException if input format is invalid or time is out of range
     */
    public static LocalDateTime parseDateTime(String dateTimeString) throws NeruneruneException {
        try {
            if (dateTimeString.length() <= 10) {
                LocalDate ld = LocalDate.parse(dateTimeString, DATE_ONLY_FORMATTER);
                return ld.atStartOfDay();
            } else {
                return LocalDateTime.parse(dateTimeString, INPUT_FORMATTER);
            }
        } catch (DateTimeParseException e) {
            throw new NeruneruneException(
                    "Invalid time detected. Hours must be between 00 and 23, and minutes between 00 and 59.\nPlease enter time in 24-hour HHmm format.");

        }
    }

    /**
     * Parses a date-time string from the storage format.
     *
     * @param dateTimeString the stored date-time string
     * @return LocalDateTime parsed from the storage string
     * @throws NeruneruneException if the string is not in the correct storage format
     */
    public static LocalDateTime parseStorageDateTime(String dateTimeString) throws NeruneruneException {
        try {
            return LocalDateTime.parse(dateTimeString, STORAGE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new NeruneruneException("Invalid storage date/time format");
        }
    }

    /**
     * Formats a LocalDateTime object into the display format used in the UI.
     *
     * @param dateTime the LocalDateTime to format
     * @return formatted date-time string for display
     */
    public static String formatForDisplay(LocalDateTime dateTime) {
        return dateTime.format(OUTPUT_FORMATTER);
    }

    /**
     * Formats a LocalDateTime object into the storage format used for saving to file.
     *
     * @param dateTime the LocalDateTime to format
     * @return formatted date-time string for storage
     */
    public static String formatForStorage(LocalDateTime dateTime) {
        return dateTime.format(STORAGE_FORMATTER);
    }
}
