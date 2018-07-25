package com.aptatek.aptatek.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.support.v4.util.Preconditions.checkArgument;

/**
 * Utility for calendar
 */

public class CalendarUtils {

    private static final int FIRST = 1;
    private static final int SECOND = 2;
    private static final int THIRD = 3;
    private static final int FOURTH = 4;
    private static final int FIFTH = 5;
    private static final int SIXTH = 6;
    private static final int SEVENTH = 7;

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

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }

    public static String formatDate(final Date date, final String pattern) {
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return sdf.format(date);
    }

    public static String dayOfWeek(final int dayOfWeek) {
        switch (dayOfWeek) {
            case FIRST:
                return SUN;
            case SECOND:
                return MON;
            case THIRD:
                return TUE;
            case FOURTH:
                return WED;
            case FIFTH:
                return THU;
            case SIXTH:
                return FRI;
            case SEVENTH:
                return SAT;
            default:
                return null;
        }
    }

    public static String dayNumberSuffix(final int n) {
        checkArgument(n >= 1 && n <= 31, "Illegal day of month: " + n);
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }
}
