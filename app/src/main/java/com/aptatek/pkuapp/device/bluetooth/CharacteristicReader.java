package com.aptatek.pkuapp.device.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

public abstract class CharacteristicReader<T> {

    private final Gson gson;

    protected CharacteristicReader(final Gson gson) {
        this.gson = gson;
    }

    protected T parseFromString(@NonNull final String value) {
        return gson.fromJson(value, getResponseClass());
    }

    @NonNull
    public T readValue(@NonNull final BluetoothGattCharacteristic characteristic) {
        return parseFromString(characteristic.getStringValue(getOffset()));
    }

    protected int getOffset() {
        return 0;
    }

    protected abstract Class<T> getResponseClass();

}
