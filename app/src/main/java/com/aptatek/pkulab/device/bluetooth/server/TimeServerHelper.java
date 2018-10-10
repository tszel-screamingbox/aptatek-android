package com.aptatek.pkulab.device.bluetooth.server;

import java.util.Calendar;

public final class TimeServerHelper {

    private static final int ONE_MINUTE_IN_MILLIS = 60 * 1000;
    private static final int FIFTEEN_MINUTE_MILLIS = 15 * ONE_MINUTE_IN_MILLIS;
    private static final int HALF_HOUR_MILLIS = 30 * ONE_MINUTE_IN_MILLIS;

    private static final byte DAY_UNKNOWN = 0;
    private static final byte DAY_MONDAY = 1;
    private static final byte DAY_TUESDAY = 2;
    private static final byte DAY_WEDNESDAY = 3;
    private static final byte DAY_THURSDAY = 4;
    private static final byte DAY_FRIDAY = 5;
    private static final byte DAY_SATURDAY = 6;
    private static final byte DAY_SUNDAY = 7;

    private static final byte DST_STANDARD = 0x0;
    private static final byte DST_HALF = 0x2;
    private static final byte DST_SINGLE = 0x4;
    private static final byte DST_DOUBLE = 0x8;
    private static final byte DST_UNKNOWN = (byte) 0xFF;

    private TimeServerHelper() {

    }

    public static byte[] getExactTime() {
        final Calendar time = Calendar.getInstance();
        final byte[] field = new byte[10];

        // Year
        final int year = time.get(Calendar.YEAR);
        field[0] = (byte) (year & 0xFF);
        field[1] = (byte) ((year >> 8) & 0xFF);
        // Month
        field[2] = (byte) (time.get(Calendar.MONTH) + 1);
        // Day
        field[3] = (byte) time.get(Calendar.DATE);
        // Hours
        field[4] = (byte) time.get(Calendar.HOUR_OF_DAY);
        // Minutes
        field[5] = (byte) time.get(Calendar.MINUTE);
        // Seconds
        field[6] = (byte) time.get(Calendar.SECOND);
        // Day of Week (1-7)
        field[7] = getDayOfWeekCode(time.get(Calendar.DAY_OF_WEEK));
        // Fractions256
        field[8] = (byte) (time.get(Calendar.MILLISECOND) / 256);
        // Adjust reason
        field[9] = 0x0;

        return field;
    }

    public static byte[] getLocalTimeInfo() {
        final Calendar time = Calendar.getInstance();
        final byte[] field = new byte[2];

        // Time zone
        final int zoneOffset = time.get(Calendar.ZONE_OFFSET) / FIFTEEN_MINUTE_MILLIS;
        field[0] = (byte) zoneOffset;

        // DST Offset
        final int dstOffset = time.get(Calendar.DST_OFFSET) / HALF_HOUR_MILLIS;
        field[1] = getDstOffsetCode(dstOffset);

        return field;
    }

    /**
     * Convert a {@link Calendar} weekday value to the corresponding
     * Bluetooth weekday code.
     */
    private static byte getDayOfWeekCode(final int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return DAY_MONDAY;
            case Calendar.TUESDAY:
                return DAY_TUESDAY;
            case Calendar.WEDNESDAY:
                return DAY_WEDNESDAY;
            case Calendar.THURSDAY:
                return DAY_THURSDAY;
            case Calendar.FRIDAY:
                return DAY_FRIDAY;
            case Calendar.SATURDAY:
                return DAY_SATURDAY;
            case Calendar.SUNDAY:
                return DAY_SUNDAY;
            default:
                return DAY_UNKNOWN;
        }
    }

    /**
     * Convert a raw DST offset (in 30 minute intervals) to the
     * corresponding Bluetooth DST offset code.
     */
    private static byte getDstOffsetCode(final int rawOffset) {
        switch (rawOffset) {
            case 0:
                return DST_STANDARD;
            case 1:
                return DST_HALF;
            case 2:
                return DST_SINGLE;
            case 4:
                return DST_DOUBLE;
            default:
                return DST_UNKNOWN;
        }
    }

}
