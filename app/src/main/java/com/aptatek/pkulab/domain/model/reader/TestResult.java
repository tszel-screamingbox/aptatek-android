package com.aptatek.pkulab.domain.model.reader;

import com.aptatek.pkulab.domain.model.PkuLevel;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TestResult {

    public abstract String getId();

    public abstract String getReaderId();

    public abstract long getTimestamp();

    public abstract PkuLevel getPkuLevel();

    public abstract boolean isSick();

    public abstract boolean isFasting();

    public static TestResult.Builder builder() {
        return new com.aptatek.pkulab.domain.model.reader.AutoValue_TestResult.Builder()
                .setSick(false)
                .setFasting(false);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setId(String id);

        public abstract Builder setReaderId(String readerId);

        public abstract Builder setTimestamp(long timestamp);

        public abstract Builder setPkuLevel(PkuLevel pkuLevel);

        public abstract Builder setSick(boolean sick);

        public abstract Builder setFasting(boolean fasting);

        public abstract TestResult build();

    }
}
