package com.aptatek.aptatek.view.settings.reminder.adapter;

import com.aptatek.aptatek.view.base.AdapterItem;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class ReminderSettingsAdapterItem implements AdapterItem {

    public abstract String getNameOfDay();

    public abstract List<RemindersAdapterItem> getReminders();

    public abstract boolean isActive();

    public abstract int getWeekDay();

    public abstract ReminderSettingsAdapterItem.Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_ReminderSettingsAdapterItem.Builder();
    }

    @Override
    public Object uniqueIdentifier() {
        return getWeekDay();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setNameOfDay(String nameOfDay);

        public abstract Builder setReminders(List<RemindersAdapterItem> reminders);

        public abstract Builder setActive(boolean active);

        public abstract Builder setWeekDay(int weekDay);

        public abstract ReminderSettingsAdapterItem build();
    }
}
