package com.aptatek.pkuapp.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CubeData {

    public abstract long getId();

    public abstract String getCubeId();

    public abstract long getTimestamp();

    public abstract PkuLevel getPkuLevel();

    public static CubeData.Builder builder() {
        return new AutoValue_CubeData.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setId(long id);

        public abstract Builder setCubeId(String cubeId);

        public abstract Builder setTimestamp(long timestamp);

        public abstract Builder setPkuLevel(PkuLevel pkuLevel);

        public abstract CubeData build();

    }
}
