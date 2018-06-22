package com.aptatek.aptatek.data;

import java.util.Date;
import java.util.List;

public class CubeData {

    private final long id;
    private final Date date;
    private final List<Measure> measureList;

    public CubeData(long id, Date date, List<Measure> measureList) {
        this.id = id;
        this.date = date;
        this.measureList = measureList;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public List<Measure> getMeasureList() {
        return measureList;
    }
}
