package com.aptatek.aptatek.domain.model;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AlertDialogModel implements Parcelable {

    @NonNull
    public abstract String getTitle();

    @NonNull
    public abstract String getMessage();

    public static AlertDialogModel create(@NonNull final String title, @NonNull final String message) {
        return new AutoValue_AlertDialogModel(title, message);
    }

}
