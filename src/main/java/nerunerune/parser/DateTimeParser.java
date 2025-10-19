package parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import exception.NeruneruneException;

public class DateTimeParser {

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a");
    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

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
                    "Invalid time detected. Hours must be between 00 and 23, and minutes between 00 and 59.\nPlease enter time in 24-hour HHMM format.");

        }
    }

    public static LocalDateTime parseStorageDateTime(String dateTimeString) throws NeruneruneException {
        try {
            return LocalDateTime.parse(dateTimeString, STORAGE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new NeruneruneException("Invalid storage date/time format");
        }
    }

    public static String formatForDisplay(LocalDateTime dateTime) {
        return dateTime.format(OUTPUT_FORMATTER);
    }
    public static String formatForStorage(LocalDateTime dateTime) {
        return dateTime.format(STORAGE_FORMATTER);
    }
}
