package com.aptatek.pkulab.util;

import com.aptatek.pkulab.domain.model.PkuLevelUnits;

public final class Constants {

    private Constants() {

    }

    public static final int ONE_HOUR_IN_MINUTES = 60;
    public static final int ONE_DAY_IN_HOURS = 24;
    public static final long DEFAULT_WETTING_PERIOD = 1000 * 60 * 30L;
    public static final float DEFAULT_PKU_NORMAL_FLOOR = 100f;
    public static final float DEFAULT_PKU_NORMAL_CEIL = 350f;
    public static final float DEFAULT_PKU_HIGH_RANGE = 150f;
    public static final float DEFAULT_PKU_LOWEST_VALUE = 2f;
    public static final float DEFAULT_PKU_HIGHEST_VALUE = 675f;
    public static final float FLOAT_COMPARISION_ERROR_MARGIN = 0.0001f;
    public static final PkuLevelUnits DEFAULT_PKU_LEVEL_UNIT = PkuLevelUnits.MILLI_GRAM;

    public static final int DAYS_OF_WEEK = 7;
    public static final long COUNTDOWN_REFRESH_PERIOD = 500L;
    public static final String REMINDER_TIMESTAMP_INTENT_KEY = "reminderTimestampIntentKey";
    public static final String REMINDER_REQUEST_CODE_INTENT_KEY = "reminderRequestCodeIntentKey";
    public static final int REMINDER_HALF_DAY = 12;
    public static final String REMINDER_RE_SCHEDULE_INTENT_KEY = "reminderReScheduleIntentKey";
    public static final int REMINDER_AM_OR_PM = 11;
    public static final int REMINDER_SPAN_COUNT = 2;
    public static final int HUNDRED_PERCENT = 100;

    public static final String REMINDER_NOTIFICATION_ACTION_TYPE_KEY = "reminderNotificationActionTypeKey";
    public static final int REMINDER_NOTIFICATION_ID = 999;
    public static final String REMINDER_DIALOG_BROADCAST_NAME = "reminderDialogBroadcastName";

    public static final long PIN_IDLE_PERIOD_MS = 20L * 1000L;
    public static final String PIN_IDLE_ACTION = "requestPinDueToInactivity";

    public static final int WETTING_COUNTDOWN_NOTIFICATION_ID = 2668;
    public static final int WETTING_FINISHED_NOTIFICATION_ID = 726;
    public static final String CONTINUE_TEST_RESULT_TYPE_KEY = "continueTestResultTypeKey";

    public static final int BT_READER_READY_NOTIFICATION_ID = 837;
    public static final int BT_READER_TEST_COMPLETE_NOTIFICATION_ID = 367;
    public static final int BT_PERMISSION_NOTIFICATION_ID = 737;
    public static final int BT_MULTIPLE_READERS_NOTIFICATION_ID = 685;
    public static final int BT_ERROR_NOTIFICATION_ID = 377;
    public static final long BT_SERVICE_IDLE_TIMEOUT = 15 * 60 * 1000L;

    public static final String URL_HELP = "https://pkulab.webflow.io/help";
    public static final String URL_PRIVACY = "https://pkulab.webflow.io/privacy";
    public static final String URL_TERMS = "https://pkulab.webflow.io/tc";

}
