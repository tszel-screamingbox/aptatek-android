package com.aptatek.pkulab.domain.model.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.model.PkuLevel;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TestResult {

    @NonNull
    public abstract String getId();

    public abstract long getDate();

    public abstract PkuLevel getPkuLevel();

    public static TestResult.Builder builder() {
        return new AutoValue_TestResult.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setId(final @NonNull String id);
        public abstract Builder setDate(final long date);
        public abstract Builder setPkuLevel(final @NonNull PkuLevel pkuLevel);
        public abstract TestResult build();

    }


}
