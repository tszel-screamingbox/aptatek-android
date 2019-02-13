package com.aptatek.pkulab.device.bluetooth.scanner;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.model.BluetoothReaderDevice;
import com.aptatek.pkulab.domain.error.DeviceDiscoveryError;
import com.aptatek.pkulab.domain.manager.reader.BluetoothScanner;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;
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
    private final Set<ReaderDevice> devices = Collections.synchronizedSet(new HashSet<>());
    private final FlowableProcessor<Boolean> scanningProcessor = BehaviorProcessor.createDefault(false);
    private final FlowableProcessor<Set<ReaderDevice>> discoveredDevicesProcessor = BehaviorProcessor.createDefault(Collections.emptySet());
    private final FlowableProcessor<DeviceDiscoveryError> discoveryErrorProcessor = BehaviorProcessor.create();

    private volatile boolean scanning;

    @Inject
    public BluetoothScannerImpl(@NonNull final ScanSettings scanSettings,
                                @NonNull final List<ScanFilter> scanFilters,
                                @NonNull final BluetoothLeScannerCompat bluetoothLeScanner) {
        this.scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(final int callbackType, final ScanResult result) {
                Timber.d("scanResult: callbackType [%d], result [%s]", callbackType, result);

                final BluetoothDevice device = result.getDevice();
                final BluetoothReaderDevice readerDevice = new BluetoothReaderDevice(device);
                if (devices.add(readerDevice)) {
                    discoveredDevicesProcessor.onNext(Collections.unmodifiableSet(devices));
                }
            }

            @Override
            public void onBatchScanResults(final List<ScanResult> results) {
                Timber.d("batchScanResults: results [%s]", results.toArray());
            }

            @Override
            public void onScanFailed(final int errorCode) {
                Timber.d("scanFailed: errorCode [%d]", errorCode);

                discoveryErrorProcessor.onNext(new DeviceDiscoveryError(errorCode));
            }
        };

        this.scanSettings = scanSettings;
        this.scanFilters = scanFilters;
        this.bluetoothLeScanner = bluetoothLeScanner;
    }

    @Override
    public Flowable<Boolean> isScanning() {
        return scanningProcessor;
    }

    @Override
    public Completable startScan() {
        return Completable.fromAction(() -> {
            if (scanning) {
                Timber.d("Attempting to call startScan while a scan is already running");
                return;
            }

            synchronized (BluetoothScannerImpl.class) {
                bluetoothLeScanner.startScan(scanFilters, scanSettings, scanCallback);
                scanning = true;
                scanningProcessor.onNext(true);
                devices.clear();
                discoveredDevicesProcessor.onNext(Collections.unmodifiableSet(devices));
            }
        });
    }

    @Override
    public Completable stopScan() {
        return Completable.fromAction(() -> {
            if (scanning) {
                synchronized (BluetoothScannerImpl.class) {
                    bluetoothLeScanner.stopScan(scanCallback);
                    scanning = false;
                    scanningProcessor.onNext(false);
                }
            }
        });
    }

    @Override
    public Flowable<Set<ReaderDevice>> getDiscoveredDevices() {
        return discoveredDevicesProcessor;
    }

    @Override
    public Flowable<DeviceDiscoveryError> getDiscoveryErrors() {
        return discoveryErrorProcessor;
    }
}
