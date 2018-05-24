package com.aptatek.aptatek.domain.model;

import com.google.auto.value.AutoValue;

import io.reactivex.annotations.Nullable;

@AutoValue
public abstract class Reading {

    @Nullable
    public abstract Long getId();

    public abstract long getTimestamp();

    public abstract float getInMilligramPerDeciliter();

    public abstract int getInMicroMol();

    public static Reading.Builder builder() {
        return new AutoValue_Reading.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setId(@Nullable Long id);

        public abstract Builder setTimestamp(long timestamp);

        public abstract Builder setInMilligramPerDeciliter(float value);

        public abstract Builder setInMicroMol(int value);

        public abstract Reading build();
    }
}
