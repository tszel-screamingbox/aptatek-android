package com.aptatek.aptatek.view.settings;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class ReminderSettingsAdapterItem {
    private String id = UUID.randomUUID().toString();
    private String nameOfDay;
    private ArrayList<RemindersAdapterItem> reminders = new ArrayList<>();
    private Boolean active = false;

    public ReminderSettingsAdapterItem(String nameOfDay) {
        this.nameOfDay = nameOfDay;
    }

    public void setNameOfDay(String nameOfDay) {
        this.nameOfDay = nameOfDay;
    }

    public Boolean getActive() {
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
        return Objects.equals(id, that.id) &&
                Objects.equals(nameOfDay, that.nameOfDay) &&
                Objects.equals(reminders, that.reminders) &&
                Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, nameOfDay, reminders, active);
    }
}
