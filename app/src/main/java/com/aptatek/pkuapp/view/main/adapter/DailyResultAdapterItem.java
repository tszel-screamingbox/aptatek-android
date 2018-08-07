package com.aptatek.pkuapp.view.main.adapter;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.aptatek.pkuapp.view.base.AdapterItem;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DailyResultAdapterItem implements AdapterItem {

    public abstract CharSequence getMeasureInformationText();

    public abstract long getTimestamp();

    public abstract @DrawableRes int getBackgroundRes();

    public abstract @ColorRes int getColorRes();

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
