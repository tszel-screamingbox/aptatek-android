package com.aptatek.pkuapp.domain.manager.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.error.DeviceDiscoveryError;
import com.aptatek.pkuapp.domain.model.ReaderDevice;

public interface BluetoothScanCallbacks {

    void onDeviceDiscovered(@NonNull ReaderDevice device);

    void onFailure(@NonNull DeviceDiscoveryError error);

}
