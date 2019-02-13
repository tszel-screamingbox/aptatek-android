package com.aptatek.pkulab.domain.manager.reader;

import com.aptatek.pkulab.domain.error.DeviceDiscoveryError;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;

import java.util.Set;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface BluetoothScanner {

    Completable startScan();

    Completable stopScan();

    Flowable<Boolean> isScanning();

    Flowable<Set<ReaderDevice>> getDiscoveredDevices();

    Flowable<DeviceDiscoveryError> getDiscoveryErrors();

}
