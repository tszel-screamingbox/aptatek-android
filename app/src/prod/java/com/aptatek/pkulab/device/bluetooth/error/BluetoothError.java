package com.aptatek.pkulab.device.bluetooth.error;

import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;

public abstract class BluetoothError extends Throwable {

    private final BluetoothDevice bluetoothDevice;

    protected BluetoothError(final @NonNull BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    @NonNull
    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }
}