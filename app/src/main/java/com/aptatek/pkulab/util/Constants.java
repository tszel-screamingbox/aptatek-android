package com.aptatek.pkulab.util;

import com.aptatek.pkulab.domain.model.PkuLevelUnits;

public final class Constants {

    private Constants() {

    }

    public static final int ONE_HOUR_IN_MINUTES = 60;
    public static final int ONE_DAY_IN_HOURS = 24;
    public static final long DEFAULT_WETTING_PERIOD = 1000 * 60;
    public static final float DEFAULT_PKU_INCREASED_FLOOR = 127f;
    public static final float DEFAULT_PKU_INCREASED_CEIL = 364f;
    public static final float DEFAULT_PKU_HIGH_RANGE = 837f;
    public static final float DEFAULT_PKU_MARGIN_MIN = 100f;
    public static final float DEFAULT_PKU_LOWEST_VALUE = 2f;
    public static final float DEFAULT_PKU_HIGHEST_VALUE = 1201f;
    public static final PkuLevelUnits DEFAULT_PKU_LEVEL_UNIT = PkuLevelUnits.MILLI_GRAM;

    public static final int DAYS_OF_WEEK = 7;
    public static final long COUNTDOWN_REFRESH_PERIOD = 500L;
    public static final String REMINDER_TIMESTAMP_INTENT_KEY = "reminderTimestampIntentKey";
    public static final String REMINDER_REQUEST_CODE_INTENT_KEY = "reminderRequestCodeIntentKey";
    public static final int REMINDER_HALF_DAY = 12;
    public static final String REMINDER_RE_SCHEDULE_INTENT_KEY = "reminderReScheduleIntentKey";
    public static final int REMINDER_AM_OR_PM = 11;
    public static final int REMINDER_MIDNIGHT = 0;
    public static final int HUNDRED_PERCENT = 100;

    public static final String REMINDER_NOTIFICATION_ACTION_TYPE_KEY = "reminderNotificationActionTypeKey";
    public static final int REMINDER_NOTIFICATION_ID = 999;
    public static final String REMINDER_DIALOG_BROADCAST_NAME = "reminderDialogBroadcastName";

    public static final long PIN_IDLE_PERIOD_MS = 20L * 1000L;
    public static final String PIN_IDLE_ACTION = "requestPinDueToInactivity";

    public static final int WETTING_COUNTDOWN_NOTIFICATION_ID = 2668;
    public static final int WETTING_FINISHED_NOTIFICATION_ID = 726;
    public static final String CONTINUE_TEST_RESULT_TYPE_KEY = "continueTestResultTypeKey";

    public static final int WORKFLOW_SATE_ERROR_NOTIFICATION_ID = 9630;

    public static final int BT_READER_READY_NOTIFICATION_ID = 837;
    public static final int BT_READER_TEST_COMPLETE_NOTIFICATION_ID = 367;
    public static final int BT_PERMISSION_NOTIFICATION_ID = 737;
    public static final int BT_MULTIPLE_READERS_NOTIFICATION_ID = 685;
    public static final int BT_ERROR_NOTIFICATION_ID = 377;
    public static final long BT_SERVICE_IDLE_TIMEOUT = 15 * 60 * 1000L;

    public static final String URL_HELP = "http://pkulabkit.com/help-app";
    public static final String URL_PRIVACY = "http://pkulabkit.com/privacy-app";
    public static final String URL_TERMS = "http://pkulabkit.com/terms-app";

    public static final String EXTRA_RESTART_NOTIFICATION_ERROR = "restart.after.bt.error";

}
