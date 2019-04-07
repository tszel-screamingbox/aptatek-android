package com.aptatek.pkulab.device.bluetooth.error;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

public class CharacteristicWriteError extends BluetoothError {

    private final int status;

    private final String characteristicId;

    public CharacteristicWriteError(@NonNull final BluetoothDevice device, final int status, final String characteristicId) {
        super(device);
        this.status = status;
        this.characteristicId = characteristicId;
    }

    public int getStatus() {
        return status;
    }

    public String getCharacteristicId() {
        return characteristicId;
    }
}