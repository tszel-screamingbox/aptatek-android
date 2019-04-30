package com.aptatek.pkulab.view.main.home.adapter.chart;


import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.view.base.list.IListTypeProvider;
import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AutoValue
public abstract class ChartVM implements IListTypeProvider {

    public abstract Date getDate();

    @Nullable
    public abstract PkuLevel getHighestPkuLevel();

    public abstract @ColorRes
    int getColorRes();

    public abstract boolean isZoomed();

    public abstract int getNumberOfMeasures();

    public abstract @StringRes
    int getState();

    @NonNull
    public abstract List<TestResult> getMeasures();

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

        public abstract Builder setState(@StringRes int stringRes);

        public abstract Builder setZoomed(boolean zoomed);

        public abstract Builder setMeasures(@NonNull List<TestResult> measures);

        public abstract Builder setNumberOfMeasures(int numberOfMeasures);

        public abstract ChartVM build();
    }

    @Override
    public int getLayoutType() {
        return R.layout.bubble_item;
    }
}
