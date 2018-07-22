package com.aptatek.aptatek.util;

import com.aptatek.aptatek.domain.model.PkuLevelUnits;

public final class Constants {

    private Constants() {

    }

    public static final long DEFAULT_INCUBATION_PERIOD = 1000 * 60 * 30L;
    public static final long DEFAULT_WETTING_PERIOD = 1000 * 60 * 10L;
    public static final float DEFAULT_PKU_NORMAL_FLOOR = 100f;
    public static final float DEFAULT_PKU_NORMAL_CEIL = 350f;
    public static final float DEFAULT_PKU_HIGH_RANGE = 150f;
    public static final float DEFAULT_PKU_LOWEST_VALUE = 2f;
    public static final float DEFAULT_PKU_HIGHEST_VALUE = 1155f;
    public static final float FLOAT_COMPARSION_ERROR_MARGIN = 0.0001f;
    public static final PkuLevelUnits DEFAULT_PKU_LEVEL_UNIT = PkuLevelUnits.MICRO_MOL;

    public static final int DAYS_OF_WEEK = 7;
    public static final long COUNTDOWN_REFRESH_PERIOD = 500L;
    public static final String REMINDER_TIMESTAMP_INTENT_KEY = "reminderTimestampIntentKey";
    public static final String REMINDER_REQUEST_CODE_INTENT_KEY = "reminderRequestCodeIntentKey";
    public static final int REMINDER_HALF_DAY = 12;
    public static final int REMINDER_AM_OR_PM = 11;
    public static final int REMINDER_SPAN_COUNT = 3;
    public static final int HUNDRED_PERCENT = 100;

}
