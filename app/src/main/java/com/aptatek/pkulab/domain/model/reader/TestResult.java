package com.aptatek.pkulab.domain.model.reader;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.model.PkuLevel;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TestResult {

    public abstract String getId();

    public abstract String getReaderId();

    public abstract long getTimestamp();

    @Nullable
    public abstract PkuLevel getPkuLevel();

    public abstract boolean isValid();

    public static TestResult.Builder builder() {
        return new com.aptatek.pkulab.domain.model.reader.AutoValue_TestResult.Builder();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setId(String id);

        public abstract Builder setReaderId(String readerId);

        public abstract Builder setTimestamp(long timestamp);

        public abstract Builder setPkuLevel(@Nullable PkuLevel pkuLevel);

        public abstract Builder setValid(final boolean isValid);

        public abstract TestResult build();

    }
}
