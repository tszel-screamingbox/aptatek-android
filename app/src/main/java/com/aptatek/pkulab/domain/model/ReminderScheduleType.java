package com.aptatek.pkulab.domain.model;

public enum ReminderScheduleType {
    WEEKLY(0, "W"),
    BIWEEKLY(1, "BW"),
    MONTHLY(2, "M");

    private int code;
    private String shortName;

    ReminderScheduleType(final int code, final String shortName) {
        this.code = code;
        this.shortName = shortName;
    }

    public int getCode() {
        return code;
    }

    public String getShortName() {
        return shortName;
    }
}

