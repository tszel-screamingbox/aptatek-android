package com.aptatek.pkulab.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

@Entity(tableName = "reminders")
public class ReminderDataModel {

    @PrimaryKey
    @NonNull
    private final String id;
    private int weekDay;
    private int hour;
    private int minute;
    @NonNull
    @TypeConverters(ReminderScheduleTypeConverter.class)
    private ReminderScheduleDataType reminderScheduleType;

    public ReminderDataModel(final String id) {
        this.id = id;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(final int weekDay) {
        this.weekDay = weekDay;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(final int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(final int minute) {
        this.minute = minute;
    }

    @NonNull
    public ReminderScheduleDataType getReminderScheduleType() {
        return reminderScheduleType;
    }

    public void setReminderScheduleType(@NonNull final ReminderScheduleDataType reminderScheduleType) {
        this.reminderScheduleType = reminderScheduleType;
    }
}
