package com.aptatek.aptatek.view.main.adapter;

import com.aptatek.aptatek.util.ChartUtils;
import com.aptatek.aptatek.view.base.AdapterItem;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DailyResultAdapterItem implements AdapterItem {

    public abstract CharSequence getMeasureInformationText();

    public abstract long getTimestamp();

    public abstract ChartUtils.State getState();

    @Override
    public Object uniqueIdentifier() {
        return getTimestamp();
    }

    public static DailyResultAdapterItem create(final CharSequence measureInformationText,
                                                final long timestamp,
                                                final ChartUtils.State state) {
        return new AutoValue_DailyResultAdapterItem(measureInformationText, timestamp, state);
    }
}
