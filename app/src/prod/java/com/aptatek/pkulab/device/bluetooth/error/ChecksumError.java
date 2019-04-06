package com.aptatek.pkulab.device.bluetooth.error;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

public class ChecksumError extends CharacteristicReadError {

    public ChecksumError(@NonNull final BluetoothDevice device, final int status, final String characteristicId) {
        super(device, status, characteristicId);
    }

}
