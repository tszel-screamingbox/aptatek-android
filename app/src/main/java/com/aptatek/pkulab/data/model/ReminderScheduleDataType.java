package com.aptatek.pkulab.data.model;

import android.arch.persistence.room.TypeConverters;

import com.aptatek.pkulab.data.model.converter.ReminderScheduleTypeConverter;

@TypeConverters(ReminderScheduleTypeConverter.class)
public enum ReminderScheduleDataType {
    WEEKLY(0),
    BIWEEKLY(1),
    MONTHLY(2);

    private int code;

    ReminderScheduleDataType(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
