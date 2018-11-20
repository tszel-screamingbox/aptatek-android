package com.aptatek.pkulab.domain.model;

public enum ReminderScheduleType {
    WEEKLY(0),
    BIWEEKLY(1),
    MONTHLY(2);

    private int code;

    ReminderScheduleType(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

