package com.aptatek.aptatek.util;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Utility for calendars
 */

public class CalendarUtils {

    /**
     * Checks whether the given 2 dates are on the same day
     *
     * @return True if the dates are on the same day, false otherwise
     */
    public static boolean isSameDay(Date date, Date otherDate) {
        final Calendar calendar1 = Calendar.getInstance();
        final Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar2.setTime(otherDate);

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }
}