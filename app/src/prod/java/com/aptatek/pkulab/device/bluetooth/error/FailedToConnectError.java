package com.aptatek.pkulab.device.bluetooth.error;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

public class FailedToConnectError extends BluetoothError {

    private final int status;

    public FailedToConnectError(@NonNull final BluetoothDevice bluetoothDevice, final int status) {
        super(bluetoothDevice);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
