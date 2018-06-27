package com.aptatek.aptatek.domain.model;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class ReminderDay {

    public abstract int getWeekDay();

    public abstract boolean isActive();

    public abstract List<Reminder> getReminders();

    public static ReminderDay.Builder builder() {
        return new AutoValue_ReminderDay.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setWeekDay(int id);

        public abstract Builder setActive(boolean active);

        public abstract Builder setReminders(List<Reminder> reminders);

        public abstract ReminderDay build();
    }
}
