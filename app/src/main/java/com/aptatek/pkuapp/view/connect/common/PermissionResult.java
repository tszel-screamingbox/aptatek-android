package com.aptatek.pkuapp.view.connect.common;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PermissionResult {

    @NonNull
    public abstract String getPermission();

    public abstract int getResult();

    public static PermissionResult create(@NonNull String permission, int result) {
        return new AutoValue_PermissionResult(permission, result);
    }

}
