package com.aptatek.pkulab.domain.model;


import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.Collection;

@AutoValue
public abstract class ReminderDay {

    public abstract int getWeekDay();

    public abstract boolean isActive();

    @Nullable
    public abstract Collection<Reminder> getReminders();

    public static ReminderDay.Builder builder() {
        return new AutoValue_ReminderDay.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setWeekDay(int id);

        public abstract Builder setActive(boolean active);

        public abstract Builder setReminders(Collection<Reminder> reminders);

        public abstract ReminderDay build();
    }
}
