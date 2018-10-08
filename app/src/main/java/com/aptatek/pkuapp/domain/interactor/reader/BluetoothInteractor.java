package com.aptatek.pkuapp.domain.interactor.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.error.DeviceDiscoveryError;
import com.aptatek.pkuapp.domain.interactor.countdown.Countdown;
import com.aptatek.pkuapp.domain.manager.reader.BluetoothScanCallbacks;
import com.aptatek.pkuapp.domain.manager.reader.BluetoothScanner;
import com.aptatek.pkuapp.domain.model.ReaderDevice;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.schedulers.Schedulers;

public class BluetoothInteractor {

    private static final long DEFAULT_SCAN_PERIOD_IN_MS = 60 * 1000L;

    private final BluetoothScanner bluetoothScanner;
    private final Set<ReaderDevice> devices = Collections.synchronizedSet(new HashSet<>());
    private final FlowableProcessor<Boolean> scanning = BehaviorProcessor.createDefault(false);
    private final FlowableProcessor<Set<ReaderDevice>> discoveredDevices = BehaviorProcessor.createDefault(Collections.emptySet());
    private final FlowableProcessor<DeviceDiscoveryError> discoveryError = BehaviorProcessor.create();

    @Inject
    public BluetoothInteractor(final BluetoothScanner bluetoothScanner) {
        this.bluetoothScanner = bluetoothScanner;

        bluetoothScanner.setCallbacks(new BluetoothScanCallbacks() {
            @Override
            public void onDeviceDiscovered(@NonNull final ReaderDevice device) {
                if (devices.add(device)) {
                    discoveredDevices.onNext(Collections.unmodifiableSet(devices));
                }
            }

            @Override
            public void onFailure(@NonNull final DeviceDiscoveryError error) {
                discoveryError.onNext(error);
                devices.clear();
            }
        });
    }

    @NonNull
    public Completable startScan(final long period) {
        if (bluetoothScanner.isScanning()) {
            return Completable.complete();
        }

        return Completable.fromAction(bluetoothScanner::startScan)
                .doOnComplete(() -> {
                    devices.clear();
                    discoveredDevices.onNext(Collections.unmodifiableSet(devices));
                    scanning.onNext(bluetoothScanner.isScanning());
                })
                .andThen(Countdown.countdown(period, ignore -> true, ignore -> ignore)
                        .take(1)
                        .flatMapCompletable(ignore -> stopScan())
                ).subscribeOn(Schedulers.computation());
    }


    @NonNull
    public Completable startScan() {
        return startScan(DEFAULT_SCAN_PERIOD_IN_MS);
    }

    @NonNull
    public Completable stopScan() {
        return Completable.fromAction(bluetoothScanner::stopScan)
                .doOnComplete(() -> scanning.onNext(bluetoothScanner.isScanning()))
                .subscribeOn(Schedulers.computation());
    }

    @NonNull
    public Flowable<Boolean> isScanning() {
        return scanning;
    }

    @NonNull
    public Flowable<Set<ReaderDevice>> getDiscoveredDevices() {
        return discoveredDevices;
    }

    @NonNull
    public Flowable<DeviceDiscoveryError> getDiscoveryError() {
        return discoveryError;
    }

}
