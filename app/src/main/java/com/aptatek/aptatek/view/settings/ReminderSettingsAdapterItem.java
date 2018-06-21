package com.aptatek.aptatek.view.settings;

import java.util.ArrayList;
import java.util.Objects;

public class ReminderSettingsAdapterItem {
    private String nameOfDay;
    private ArrayList<RemindersAdapterItem> reminders;
    private Boolean active;
    private int weekDay;

    public ReminderSettingsAdapterItem(int weekDay, String nameOfDay, Boolean active, ArrayList<RemindersAdapterItem> reminders) {
        this.reminders = reminders;
        this.nameOfDay = nameOfDay;
        this.active = active;
        this.weekDay = weekDay;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public void setNameOfDay(String nameOfDay) {
        this.nameOfDay = nameOfDay;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getNameOfDay() {
        return nameOfDay;
    }

    public ArrayList<RemindersAdapterItem> getReminders() {
        return reminders;
    }

    public void setReminders(ArrayList<RemindersAdapterItem> reminders) {
        this.reminders = reminders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReminderSettingsAdapterItem that = (ReminderSettingsAdapterItem) o;
        return weekDay == that.weekDay &&
                Objects.equals(nameOfDay, that.nameOfDay) &&
                Objects.equals(reminders, that.reminders) &&
                Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nameOfDay, reminders, active, weekDay);
    }
}
