package com.aptatek.pkulab.domain.manager.reader;

import android.support.annotation.Nullable;

public interface BluetoothScanner {

    boolean isScanning();

    void startScan();

    void stopScan();

    void setCallbacks(@Nullable BluetoothScanCallbacks callbacks);

}
