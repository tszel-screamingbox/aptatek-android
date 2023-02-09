package com.aptatek.pkulab.domain.manager.reader;

import com.aptatek.pkulab.device.bluetooth.error.BluetoothDisabledError;

public interface BluetoothAdapter {

    boolean isEnabled();

    void enable() throws BluetoothDisabledError;

}
