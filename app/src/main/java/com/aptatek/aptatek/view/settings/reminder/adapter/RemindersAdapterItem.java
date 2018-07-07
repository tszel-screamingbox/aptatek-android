package com.aptatek.aptatek.view.settings.reminder.adapter;

import com.aptatek.aptatek.view.base.AdapterItem;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RemindersAdapterItem implements AdapterItem {
    public abstract String getId();

    public abstract String getTime();

    public abstract boolean isActive();

    public abstract int getHour();

    public abstract int getMinute();

    public static Builder builder() {
        return new AutoValue_RemindersAdapterItem.Builder();
    }

    @Override
    public Object uniqueIdentifier() {
        return getId();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(String id);

        public abstract Builder setTime(String time);

        public abstract Builder setActive(boolean active);

        public abstract Builder setHour(int hour);

        public abstract Builder setMinute(int minute);

        public abstract RemindersAdapterItem build();
    }
}
