package com.aptatek.aptatek.data.chart;

import java.util.Date;

public class CubeData {

    private final long id;
    private final Date date;
    private final int measuredLevel;
    private final double unit;

    public CubeData(final long id, final Date date, final int measuredLevel, final double unit) {
        this.id = id;
        this.date = date;
        this.measuredLevel = measuredLevel;
        this.unit = unit;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getMeasuredLevel() {
        return measuredLevel;
    }

    public double getUnit() {
        return unit;
    }
}
