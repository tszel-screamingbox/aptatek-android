package com.aptatek.pkulab.view.connect.common;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class MissingPermission {

    public abstract String getPermission();

    public abstract boolean shouldShowRationale();

    public static MissingPermission create(@NonNull final String permission, final boolean showRationale) {
        return new AutoValue_MissingPermission(permission, showRationale);
    }

}
