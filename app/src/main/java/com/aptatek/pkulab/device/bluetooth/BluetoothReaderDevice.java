package com.aptatek.pkulab.device.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.aptatek.pkulab.domain.model.ReaderDevice;

import java.util.Objects;

public class BluetoothReaderDevice implements ReaderDevice {

    private final BluetoothDevice bluetoothDevice;

    public BluetoothReaderDevice(@NonNull BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    @Override
    public String getName() {
        final String name = bluetoothDevice.getName();
        return TextUtils.isEmpty(name) ? "" : name;
    }

    @Override
    public String getMac() {
        return bluetoothDevice.getAddress();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BluetoothReaderDevice that = (BluetoothReaderDevice) o;
        return Objects.equals(bluetoothDevice.getAddress(), that.bluetoothDevice.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(bluetoothDevice.getAddress());
    }
}
