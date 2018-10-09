package com.aptatek.pkulab.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CubeData {

    public abstract long getId();

    public abstract String getCubeId();

    public abstract long getTimestamp();

    public abstract PkuLevel getPkuLevel();

    public abstract boolean isSick();

    public abstract boolean isFasting();

    public static CubeData.Builder builder() {
        return new AutoValue_CubeData.Builder()
                .setSick(false)
                .setFasting(false);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setId(long id);

        public abstract Builder setCubeId(String cubeId);

        public abstract Builder setTimestamp(long timestamp);

        public abstract Builder setPkuLevel(PkuLevel pkuLevel);

        public abstract Builder setSick(boolean sick);

        public abstract Builder setFasting(boolean fasting);

        public abstract CubeData build();

    }
}
