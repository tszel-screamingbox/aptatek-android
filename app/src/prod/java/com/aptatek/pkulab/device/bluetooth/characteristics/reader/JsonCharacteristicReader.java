package com.aptatek.pkulab.device.bluetooth.characteristics.reader;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import no.nordicsemi.android.ble.data.Data;
import timber.log.Timber;

public abstract class JsonCharacteristicReader<T> implements CharacteristicReader<T> {

    private final Gson gson;

    protected JsonCharacteristicReader(final Gson gson) {
        this.gson = gson;
    }

    protected T parseFromString(@NonNull final String value) {
        return gson.fromJson(value, getResponseClass());
    }

    @Override
    public T read(@NonNull final Data data) {
        final String stringValue = data.getStringValue(getOffset());
        Timber.d("read stringValue: %s", stringValue);

        if (stringValue == null) {
            return null; // TODO throw exception instead?
        }

        return parseFromString(stringValue);
    }

    protected int getOffset() {
        return 0;
    }

    protected abstract Class<T> getResponseClass();

}
