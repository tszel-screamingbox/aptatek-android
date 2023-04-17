package com.aptatek.pkulab.view.connect.turnreaderon;


import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SyncProgress implements Parcelable {

    public abstract int getCurrent();
    public abstract int getFailed();
    public abstract int getTotal();

    public static SyncProgress create(final int current, final int failed, final int total) {
        return new AutoValue_SyncProgress(current, failed, total);
    }

}
