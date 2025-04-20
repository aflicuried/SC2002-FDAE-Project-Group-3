package util;

import java.time.LocalDate;

public class DateUtil {
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static boolean isWithinProjectPeriod(LocalDate openingDate, LocalDate closingDate) {
        LocalDate today = getCurrentDate();
        return !today.isBefore(openingDate) && !today.isAfter(closingDate);
    }

    public static boolean hasDateOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }
}