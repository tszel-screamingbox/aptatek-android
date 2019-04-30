package com.aptatek.pkulab.device.bluetooth.error;

import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;

public class FailedToBondError extends BluetoothError {

    private final int status;

    public FailedToBondError(@NonNull final BluetoothDevice bluetoothDevice, final int status) {
        super(bluetoothDevice);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
