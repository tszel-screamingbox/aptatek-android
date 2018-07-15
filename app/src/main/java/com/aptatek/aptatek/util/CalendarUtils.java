package com.aptatek.aptatek.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utility for calendar
 */

public class CalendarUtils {

    private static final String SUN = "Sunday";
    private static final String MON = "Monday";
    private static final String TUE = "Tuesday";
    private static final String WED = "Wednesday";
    private static final String THU = "Thursday";
    private static final String FRI = "Friday";
    private static final String SAT = "Saturday";

    /**
     * Checks whether the given 2 dates are on the same day
     *
     * @return True if the dates are on the same day, false otherwise
     */
    public static boolean isSameDay(final Date date, final Date otherDate) {
        final Calendar calendar1 = Calendar.getInstance();
        final Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar2.setTime(otherDate);

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }

    public static String formatDate(final Date date, final String pattern) {
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return sdf.format(date);
    }

    public static String dayOfWeek(final int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return SUN;
            case 2:
                return MON;
            case 3:
                return TUE;
            case 4:
                return WED;
            case 5:
                return THU;
            case 6:
                return FRI;
            case 7:
                return SAT;
            default:
                return null;
        }
    }
}