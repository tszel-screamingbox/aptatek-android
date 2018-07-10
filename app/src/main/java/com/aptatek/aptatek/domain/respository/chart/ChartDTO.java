package com.aptatek.aptatek.domain.respository.chart;

import java.util.Date;
import java.util.List;

public class ChartDTO {

    private final long id;
    private final Date date;
    private final List<Measure> measureList;
    private final float bubbleYAxis;
    private final float startLineYAxis;
    private final float endLineYAxis;

    public ChartDTO(final long id, final Date date, final List<Measure> measureList, final float bubbleYAxis, final float startLineYAxis, final float endLineYAxis) {
        this.id = id;
        this.date = date;
        this.measureList = measureList;
        this.bubbleYAxis = bubbleYAxis;
        this.startLineYAxis = startLineYAxis;
        this.endLineYAxis = endLineYAxis;
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

    public float getBubbleYAxis() {
        return bubbleYAxis;
    }

    public float getStartLineYAxis() {
        return startLineYAxis;
    }

    public float getEndLineYAxis() {
        return endLineYAxis;
    }
}
