package com.aptatek.aptatek.domain.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class IncubationCountdown {

    public abstract String getRemainingFormattedText();

    public abstract long getRemainingMillis();

    public static Builder builder() {
        return new AutoValue_IncubationCountdown.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setRemainingFormattedText(@NonNull String remainingFormattedText);

        public abstract Builder setRemainingMillis(long remainingMillis);

        public abstract IncubationCountdown build();

    }
}
