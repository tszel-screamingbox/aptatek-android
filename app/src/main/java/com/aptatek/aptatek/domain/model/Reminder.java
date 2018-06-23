package com.aptatek.aptatek.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Reminder {

    public abstract String getId();

    public abstract int getHour();

    public abstract int getMinute();

    public abstract int getWeekDay();

    public static Reminder.Builder builder() {
        return new AutoValue_Reminder.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setId(String id);

        public abstract Builder setHour(int hour);

        public abstract Builder setMinute(int minute);

        public abstract Builder setWeekDay(int weekDay);

        public abstract Reminder build();
    }
}
