package com.aptatek.aptatek.view.settings.reminder;

import java.util.ArrayList;
import java.util.Objects;

// TODO make it IMMUTABLE
public class ReminderSettingsAdapterItem {
    private String nameOfDay;
    private ArrayList<RemindersAdapterItem> reminders;
    private Boolean active;
    private int weekDay;

    public ReminderSettingsAdapterItem(final int weekDay, final String nameOfDay, final Boolean active, final ArrayList<RemindersAdapterItem> reminders) {
        this.reminders = reminders;
        this.nameOfDay = nameOfDay;
        this.active = active;
        this.weekDay = weekDay;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(final int weekDay) {
        this.weekDay = weekDay;
    }

    public void setNameOfDay(final String nameOfDay) {
        this.nameOfDay = nameOfDay;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public String getNameOfDay() {
        return nameOfDay;
    }

    public ArrayList<RemindersAdapterItem> getReminders() {
        return reminders;
    }

    public void setReminders(final ArrayList<RemindersAdapterItem> reminders) {
        this.reminders = reminders;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ReminderSettingsAdapterItem that = (ReminderSettingsAdapterItem) o;
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
