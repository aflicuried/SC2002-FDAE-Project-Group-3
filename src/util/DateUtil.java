package util;

import java.time.LocalDate;

/**
 * Utility class for handling date-related operations such as retrieving the current date,
 * checking if a date is within a given project period, and detecting date range overlaps.
 */
public class DateUtil {

    /**
     * Returns the current system date.
     *
     * @return the current date as {@link LocalDate}
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * Checks if the current date is within the specified project period.
     *
     * @param openingDate the start date of the project
     * @param closingDate the end date of the project
     * @return true if the current date is within the period, inclusive; false otherwise
     */
    public static boolean isWithinProjectPeriod(LocalDate openingDate, LocalDate closingDate) {
        LocalDate today = getCurrentDate();
        return !today.isBefore(openingDate) && !today.isAfter(closingDate);
    }

    /**
     * Checks if two date ranges overlap.
     *
     * @param start1 the start date of the first range
     * @param end1   the end date of the first range
     * @param start2 the start date of the second range
     * @param end2   the end date of the second range
     * @return true if the date ranges overlap; false otherwise
     */
    public static boolean hasDateOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }
}
