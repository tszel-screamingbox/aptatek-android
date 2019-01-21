package com.aptatek.pkulab.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

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
    @TypeConverters(ReminderScheduleTypeConverter.class)
    private PkuLevel pkuLevel;
    private boolean sick;
    private boolean fasting;

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
}