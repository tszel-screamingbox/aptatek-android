package com.aptatek.pkulab.device.bluetooth.error;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

public class NoValueReceivedError extends BluetoothError {

    public NoValueReceivedError(@NonNull BluetoothDevice bluetoothDevice) {
        super(bluetoothDevice);
    }
}
