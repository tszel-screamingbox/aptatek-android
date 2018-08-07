package com.aptatek.aptatek.view.main.adapter;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.model.CubeData;
import com.aptatek.aptatek.domain.model.PkuLevel;
import com.aptatek.aptatek.view.base.list.IListTypeProvider;
import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AutoValue
public abstract class ChartVM implements IListTypeProvider {

    public abstract Date getDate();

    @Nullable
    public abstract PkuLevel getHighestPkuLevel();

    public abstract @ColorRes int getColorRes();

    public abstract @DrawableRes int getCollapsedBackgroundRes();

    public abstract @DrawableRes int getExpandedBackgroundRes();

    public abstract float getBubbleYAxis();

    public abstract float getStartLineYAxis();

    public abstract float getEndLineYAxis();

    public abstract boolean isZoomed();

    public abstract int getNumberOfMeasures();

    @NonNull
    public abstract List<CubeData> getMeasures();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_ChartVM.Builder()
                .setMeasures(new ArrayList<>())
                .setNumberOfMeasures(0);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setDate(Date date);

        public abstract Builder setHighestPkuLevel(@Nullable PkuLevel highestMeasure);

        public abstract Builder setColorRes(@ColorRes int colorRes);

        public abstract Builder setCollapsedBackgroundRes(@DrawableRes int backgroundRes);

        public abstract Builder setExpandedBackgroundRes(@DrawableRes int backgroundRes);

        public abstract Builder setBubbleYAxis(float bubbleYAxis);

        public abstract Builder setStartLineYAxis(float startLineYAxis);

        public abstract Builder setEndLineYAxis(float endLineYAxis);

        public abstract Builder setZoomed(boolean zoomed);

        public abstract Builder setMeasures(@NonNull List<CubeData> measures);

        public abstract Builder setNumberOfMeasures(int numberOfMeasures);

        public abstract ChartVM build();
    }

    @Override
    public int getLayoutType() {
        return R.layout.bubble_item;
    }
}
