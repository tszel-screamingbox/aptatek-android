package com.aptatek.pkulab.domain.model.reader;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.model.PkuLevel;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TestResult {

    public abstract String getId();

    public abstract String getReaderId();

    public abstract long getTimestamp();

    public abstract long getEndTimestamp();

    @Nullable
    public abstract PkuLevel getPkuLevel();

    public abstract boolean isValid();

    @Nullable
    public abstract String getOverallResult();

    @Nullable
    public abstract String getTemperature();

    @Nullable
    public abstract String getHumidity();

    @Nullable
    public abstract String getHardwareVersion();

    @Nullable
    public abstract String getFirmwareVersion();

    @Nullable
    public abstract String getSoftwareVersion();

    @Nullable
    public abstract String getConfigHash();

    @Nullable
    public abstract Long getCassetteLot();

    public abstract long getCassetteExpiry();

    @Nullable
    public abstract String getAssayHash();

    public abstract String getAssayVersion();

    @Nullable
    public abstract String getAssay();

    @Nullable
    public abstract String getReaderMode();

    public abstract String getRawResponse();

    @Nullable
    public abstract String getReaderMac();

    public static TestResult.Builder builder() {
        return new com.aptatek.pkulab.domain.model.reader.AutoValue_TestResult.Builder();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setId(String id);

        public abstract Builder setReaderId(String readerId);

        public abstract Builder setReaderMac(@Nullable String mac);

        public abstract Builder setTimestamp(long timestamp);

        public abstract Builder setEndTimestamp(long timestamp);

        public abstract Builder setOverallResult(@Nullable String overallResult);

        public abstract Builder setTemperature(@Nullable String temperature);

        public abstract Builder setHumidity(@Nullable String humidity);

        public abstract Builder setFirmwareVersion(@Nullable String fwVersion);

        public abstract Builder setHardwareVersion(@Nullable String hwVersion);

        public abstract Builder setSoftwareVersion(@Nullable String swVersion);

        public abstract Builder setConfigHash(@Nullable String configHash);

        public abstract Builder setCassetteLot(@Nullable Long cassetteLot);

        public abstract Builder setAssayHash(@Nullable String assayHash);

        public abstract Builder setAssayVersion(@Nullable String assayVersion);

        public abstract Builder setAssay(@Nullable String assay);

        public abstract Builder setPkuLevel(@Nullable PkuLevel pkuLevel);

        public abstract Builder setValid(final boolean isValid);

        public abstract Builder setReaderMode(@Nullable String readerMode);

        public abstract Builder setCassetteExpiry(final long expiry);

        public abstract Builder setRawResponse(final String rawResponse);

        public abstract TestResult build();

    }
}
