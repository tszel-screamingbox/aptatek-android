package com.aptatek.pkulab.device.bluetooth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aptatek.pkulab.domain.error.DeviceDiscoveryError;
import com.aptatek.pkulab.domain.manager.reader.BluetoothScanCallbacks;
import com.aptatek.pkulab.domain.manager.reader.BluetoothScanner;

import java.util.List;

import javax.inject.Inject;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;
import timber.log.Timber;

public class BluetoothScannerImpl implements BluetoothScanner {

    private final ScanCallback scanCallback;
    private final ScanSettings scanSettings;
    private final List<ScanFilter> scanFilters;
    private final BluetoothLeScannerCompat bluetoothLeScanner;

    private volatile boolean scanning;

    private BluetoothScanCallbacks callbacks;

    @Inject
    public BluetoothScannerImpl(@NonNull final ScanSettings scanSettings,
                                @NonNull final List<ScanFilter> scanFilters,
                                @NonNull final BluetoothLeScannerCompat bluetoothLeScanner) {
        this.scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(final int callbackType, final ScanResult result) {
                Timber.d("scanResult: callbackType [%d], result [%s]", callbackType, result);

                if (callbacks != null) {
                    callbacks.onDeviceDiscovered(new BluetoothReaderDevice(result.getDevice()));
                }
            }

            @Override
            public void onBatchScanResults(final List<ScanResult> results) {
                Timber.d("batchScanResults: results [%s]", results.toArray());
            }

            @Override
            public void onScanFailed(final int errorCode) {
                Timber.d("scanFailed: errorCode [%d]", errorCode);

                if (callbacks != null) {
                    callbacks.onFailure(new DeviceDiscoveryError(errorCode));
                }
            }
        };

        this.scanSettings = scanSettings;
        this.scanFilters = scanFilters;
        this.bluetoothLeScanner = bluetoothLeScanner;
    }

    @Override
    public boolean isScanning() {
        return scanning;
    }

    @Override
    public void startScan() {
        if (scanning) {
            Timber.d("Attempting to call startScan while a scan is already running");
            return;
        }

        synchronized (BluetoothScannerImpl.class) {
            bluetoothLeScanner.startScan(scanFilters, scanSettings, scanCallback);
            scanning = true;
        }
    }

    @Override
    public void stopScan() {
        if (scanning) {
            synchronized (BluetoothScannerImpl.class) {
                bluetoothLeScanner.stopScan(scanCallback);
                scanning = false;
            }
        }
    }

    @Override
    public void setCallbacks(@Nullable final BluetoothScanCallbacks callbacks) {
        this.callbacks = callbacks;
    }
}
