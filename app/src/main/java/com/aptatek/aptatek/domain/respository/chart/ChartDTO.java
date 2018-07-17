package com.aptatek.aptatek.domain.respository.chart;

import com.google.auto.value.AutoValue;

import java.util.Date;
import java.util.List;

@AutoValue
public abstract class ChartDTO {

    public abstract long getId();

    public abstract Date getDate();

    public abstract List<Measure> getMeasureList();

    public abstract float getBubbleYAxis();

    public abstract float getStartLineYAxis();

    public abstract float getEndLineYAxis();

    public static ChartDTO create(final long id, final Date date, final List<Measure> measureList, final float bubbleYAxis, final float startLineYAxis, final float endLineYAxis) {
        return new AutoValue_ChartDTO(id, date, measureList, bubbleYAxis, startLineYAxis, endLineYAxis);
    }
}
