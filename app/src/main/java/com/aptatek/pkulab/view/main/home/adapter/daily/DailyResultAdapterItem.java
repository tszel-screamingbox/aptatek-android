package com.aptatek.pkulab.view.main.home.adapter.daily;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;


import com.aptatek.pkulab.view.base.AdapterItem;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DailyResultAdapterItem implements AdapterItem {

    public abstract CharSequence getMeasureInformationText();

    public abstract long getTimestamp();

    public abstract @DrawableRes
    int getBackgroundRes();

    public abstract @ColorRes
    int getColorRes();

    @Override
    public Object uniqueIdentifier() {
        return getTimestamp();
    }

    public static DailyResultAdapterItem create(final CharSequence measureInformationText,
                                                final long timestamp,
                                                final @DrawableRes int backgroundRes,
                                                final @ColorRes int colorRes) {
        return new AutoValue_DailyResultAdapterItem(measureInformationText, timestamp, backgroundRes, colorRes);
    }
}
