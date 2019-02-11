package com.aptatek.pkulab.domain.model.reader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ConnectionEvent {

    @Nullable
    public abstract ReaderDevice getDevice();

    @NonNull
    public abstract ConnectionState getConnectionState();

    public static ConnectionEvent create(@Nullable final ReaderDevice device, @NonNull final ConnectionState state) {
        return new AutoValue_ConnectionEvent(device, state);
    }
}
