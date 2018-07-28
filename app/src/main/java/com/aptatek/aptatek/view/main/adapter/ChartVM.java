package com.aptatek.aptatek.view.main.adapter;

import android.support.annotation.Nullable;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.respository.chart.Measure;
import com.aptatek.aptatek.view.base.list.IListTypeProvider;
import com.google.auto.value.AutoValue;

import java.util.Date;
import java.util.List;

@AutoValue
public abstract class ChartVM implements IListTypeProvider {

    public abstract long getId();

    public abstract Date getDate();

    @Nullable
    public abstract Measure getHighestMeasure();

    public abstract float getBubbleYAxis();

    public abstract float getStartLineYAxis();

    public abstract float getEndLineYAxis();

    public abstract boolean isZoomed();

    public abstract int getNumberOfMeasures();

    @Nullable
    public abstract List<Measure> getMeasures();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_ChartVM.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(long id);

        public abstract Builder setDate(Date date);

        public abstract Builder setHighestMeasure(@Nullable Measure highestMeasure);

        public abstract Builder setBubbleYAxis(float bubbleYAxis);

        public abstract Builder setStartLineYAxis(float startLineYAxis);

        public abstract Builder setEndLineYAxis(float endLineYAxis);

        public abstract Builder setZoomed(boolean zoomed);

        public abstract Builder setMeasures(@Nullable List<Measure> measures);

        public abstract Builder setNumberOfMeasures(int numberOfMeasures);

        public abstract ChartVM build();
    }

    @Override
    public int getLayoutType() {
        return R.layout.bubble_item;
    }
}
