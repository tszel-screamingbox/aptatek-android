package com.aptatek.pkulab.data.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "test_results")
public class TestResultDataModel {

    @PrimaryKey
    @NonNull
    private String id;
    @NonNull
    private String readerId;
    @Nullable
    private String readerMac;
    private long timestamp;
    private long endTimestamp;
    private float numericValue;
    private String unit;
    private String textResult;
    private boolean sick;
    private boolean fasting;
    private boolean valid;
    @Nullable
    private String overallResult;
    @Nullable
    private String temperature;
    @Nullable
    private String humidity;
    @Nullable
    private String hardwareVersion;
    @Nullable
    private String softwareVersion;
    @Nullable
    private String firmwareVersion;
    @Nullable
    private String configHash;
    private long cassetteLot;
    @Nullable
    private String assayHash;
    @Nullable
    private String assayVersion;
    @Nullable
    private String assay;
    @Nullable
    private String readerMode;
    private long cassetteExpiry;
    private String rawResponse;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(final @NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(final @NonNull String readerId) {
        this.readerId = readerId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public float getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(float numericValue) {
        this.numericValue = numericValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTextResult() {
        return textResult;
    }

    public void setTextResult(String textResult) {
        this.textResult = textResult;
    }

    public boolean isSick() {
        return sick;
    }

    public void setSick(boolean sick) {
        this.sick = sick;
    }

    public boolean isFasting() {
        return fasting;
    }

    public void setFasting(boolean fasting) {
        this.fasting = fasting;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    @Nullable
    public String getOverallResult() {
        return overallResult;
    }

    public void setOverallResult(@Nullable String overallResult) {
        this.overallResult = overallResult;
    }

    @Nullable
    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(@Nullable String temperature) {
        this.temperature = temperature;
    }

    @Nullable
    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(@Nullable String humidity) {
        this.humidity = humidity;
    }

    @Nullable
    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(@Nullable String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    @Nullable
    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(@Nullable String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    @Nullable
    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(@Nullable String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    @Nullable
    public String getConfigHash() {
        return configHash;
    }

    public void setConfigHash(@Nullable String configHash) {
        this.configHash = configHash;
    }

    public long getCassetteLot() {
        return cassetteLot;
    }

    public void setCassetteLot(long cassetteLot) {
        this.cassetteLot = cassetteLot;
    }

    @Nullable
    public String getAssayHash() {
        return assayHash;
    }

    public void setAssayHash(@Nullable String assayHash) {
        this.assayHash = assayHash;
    }

    @Nullable
    public String getAssayVersion() {
        return assayVersion;
    }

    public void setAssayVersion(@Nullable String assayVersion) {
        this.assayVersion = assayVersion;
    }

    @Nullable
    public String getAssay() {
        return assay;
    }

    public void setAssay(@Nullable String assay) {
        this.assay = assay;
    }

    @Nullable
    public String getReaderMode() {
        return readerMode;
    }

    public void setReaderMode(@Nullable String readerMode) {
        this.readerMode = readerMode;
    }

    public long getCassetteExpiry() {
        return cassetteExpiry;
    }

    public void setCassetteExpiry(long cassetteExpiry) {
        this.cassetteExpiry = cassetteExpiry;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    @Nullable
    public String getReaderMac() {
        return readerMac;
    }

    public void setReaderMac(@Nullable String readerMac) {
        this.readerMac = readerMac;
    }
}
