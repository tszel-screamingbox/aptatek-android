package com.aptatek.aptatek.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "readings")
public class ReadingDataModel {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long timestamp;

    @ColumnInfo(name = "milligram_per_deciliter")
    private float mgPerDlValue;

    @ColumnInfo(name = "micromol")
    private int microMolValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getMgPerDlValue() {
        return mgPerDlValue;
    }

    public void setMgPerDlValue(float mgPerDlValue) {
        this.mgPerDlValue = mgPerDlValue;
    }

    public int getMicroMolValue() {
        return microMolValue;
    }

    public void setMicroMolValue(int microMolValue) {
        this.microMolValue = microMolValue;
    }
}
