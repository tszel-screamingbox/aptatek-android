package com.aptatek.pkuapp.domain.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ReaderConnectionEvent {

    @Nullable
    public abstract ReaderDevice getDevice();

    @NonNull
    public abstract ReaderConnectionState getConnectionState();

    public static ReaderConnectionEvent create(@Nullable final ReaderDevice device, @NonNull final ReaderConnectionState state) {
        return new AutoValue_ReaderConnectionEvent(device, state);
    }
}
