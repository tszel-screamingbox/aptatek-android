package com.aptatek.pkulab.data.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.aptatek.pkulab.data.model.converter.PkuLevelTypeConverter;
import com.aptatek.pkulab.data.model.converter.ReminderScheduleTypeConverter;
import com.aptatek.pkulab.domain.model.PkuLevel;

@Entity(tableName = "test_results")
public class TestResultDataModel {

    @PrimaryKey
    @NonNull
    private String id;
    @NonNull
    private String readerId;
    private long timestamp;
    @NonNull
    @TypeConverters(PkuLevelTypeConverter.class)
    private PkuLevel pkuLevel;
    private boolean sick;
    private boolean fasting;
    private boolean valid;

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

    @NonNull
    public PkuLevel getPkuLevel() {
        return pkuLevel;
    }

    public void setPkuLevel(final @NonNull PkuLevel pkuLevel) {
        this.pkuLevel = pkuLevel;
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
}
