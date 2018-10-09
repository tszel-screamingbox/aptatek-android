package com.aptatek.pkulab.domain.manager.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.error.DeviceDiscoveryError;
import com.aptatek.pkulab.domain.model.ReaderDevice;

public interface BluetoothScanCallbacks {

    void onDeviceDiscovered(@NonNull ReaderDevice device);

    void onFailure(@NonNull DeviceDiscoveryError error);

}
