package com.aptatek.aptatek.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

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

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public static List<ReminderDayDataModel> creator() {
        List<ReminderDayDataModel> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            days.add(new ReminderDayDataModel());
        }

        return days;
    }
}
