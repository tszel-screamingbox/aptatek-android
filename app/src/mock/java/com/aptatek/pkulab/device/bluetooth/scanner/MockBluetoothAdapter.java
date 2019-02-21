package com.aptatek.pkulab.device.bluetooth.scanner;

import com.aptatek.pkulab.domain.manager.reader.BluetoothAdapter;

public class MockBluetoothAdapter implements BluetoothAdapter {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void enable() {
        // do nothing
    }
}
