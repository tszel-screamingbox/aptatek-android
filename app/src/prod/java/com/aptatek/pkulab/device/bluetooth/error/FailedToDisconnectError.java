package com.aptatek.pkulab.device.bluetooth.error;

import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;

public class FailedToDisconnectError extends BluetoothError {

    private final int status;


    public FailedToDisconnectError(@NonNull final BluetoothDevice device, final int status) {
        super(device);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
