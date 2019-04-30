package com.aptatek.pkulab.data.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.aptatek.pkulab.util.Constants;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "reminderDays")
public class ReminderDayDataModel {

    @PrimaryKey(autoGenerate = true)
    private int weekDay;

    @ColumnInfo(name = "active")
    private boolean active;

    public ReminderDayDataModel() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(final int weekDay) {
        this.weekDay = weekDay;
    }

    public static List<ReminderDayDataModel> creator() {
        final List<ReminderDayDataModel> days = new ArrayList<>();
        for (int i = 0; i < Constants.DAYS_OF_WEEK; i++) {
            days.add(new ReminderDayDataModel());
        }

        return days;
    }
}
