package com.aptatek.pkulab.domain.model.reader;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Error {

    @NonNull
    public abstract String getMessage();

    public static Error create(final @NonNull String message) {
        return new AutoValue_Error(message);
    }
}
