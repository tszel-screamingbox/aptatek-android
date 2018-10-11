package com.aptatek.pkulab.device.bluetooth.reader;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import javax.inject.Inject;

public class CharacteristicWriter {

    private final Gson gson;

    @Inject
    public CharacteristicWriter(final Gson gson) {
        this.gson = gson;
    }

    public byte[] toBytes(@NonNull final Object value) {
        return gson.toJson(value).getBytes();
    }

    public void writeValue(@NonNull final BluetoothGattCharacteristic characteristic, final Object response) {
        characteristic.setValue(toBytes(response));
    }
}
