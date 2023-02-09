package com.aptatek.pkulab.device.bluetooth.scanner;

import com.aptatek.pkulab.device.bluetooth.error.BluetoothDisabledError;
import com.aptatek.pkulab.device.bluetooth.error.BluetoothError;
import com.aptatek.pkulab.domain.manager.reader.BluetoothAdapter;

import timber.log.Timber;

public class BluetoothAdapterImpl implements BluetoothAdapter {

    private final android.bluetooth.BluetoothAdapter bluetoothAdapter;

    public BluetoothAdapterImpl(final android.bluetooth.BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    @Override
    public boolean isEnabled() {
        if (bluetoothAdapter == null) {
            // This should not happen since we strictly require the device to have Bluetooth in the Manifest.
            Timber.e("No bluetooth adapter!");
        } else {
            return bluetoothAdapter.isEnabled();
        }

        return false;
    }

    @Override
    public void enable() throws BluetoothDisabledError {
        if (bluetoothAdapter == null) {
            // This should not happen since we strictly require the device to have Bluetooth in the Manifest.
            Timber.e("No bluetooth adapter!");
        } else {
            final boolean isEnabled = bluetoothAdapter.enable();
            if (!isEnabled) {
                throw new BluetoothDisabledError();
            }
        }
    }

}
