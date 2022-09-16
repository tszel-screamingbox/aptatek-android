package com.aptatek.pkulab.device.bluetooth.error;

import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;

public class ChecksumError extends CharacteristicReadError {

    private final String message;

    public ChecksumError(@NonNull final BluetoothDevice device, final int status, final String characteristicId, final String message) {
        super(device, status, characteristicId);
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChecksumError{" +
                "message='" + message + '\'' +
                '}';
    }
}
