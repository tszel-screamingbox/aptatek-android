package com.aptatek.pkulab.domain.interactor.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.error.DeviceDiscoveryError;
import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.domain.manager.reader.BluetoothAdapter;
import com.aptatek.pkulab.domain.manager.reader.BluetoothConditionChecker;
import com.aptatek.pkulab.domain.manager.reader.BluetoothScanCallbacks;
import com.aptatek.pkulab.domain.manager.reader.BluetoothScanner;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;

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
    private final BluetoothConditionChecker bluetoothConditionChecker;
    private final BluetoothAdapter bluetoothAdapter;
    private final Set<ReaderDevice> devices = Collections.synchronizedSet(new HashSet<>());
    private final FlowableProcessor<Boolean> scanning = BehaviorProcessor.createDefault(false);
    private final FlowableProcessor<Set<ReaderDevice>> discoveredDevices = BehaviorProcessor.createDefault(Collections.emptySet());
    private final FlowableProcessor<DeviceDiscoveryError> discoveryError = BehaviorProcessor.create();

    @Inject
    public BluetoothInteractor(final BluetoothScanner bluetoothScanner,
                               final BluetoothConditionChecker bluetoothConditionChecker,
                               final BluetoothAdapter bluetoothAdapter) {
        this.bluetoothScanner = bluetoothScanner;
        this.bluetoothConditionChecker = bluetoothConditionChecker;
        this.bluetoothAdapter = bluetoothAdapter;

        bluetoothScanner.setCallbacks(new BluetoothScanCallbacks() {
            @Override
            public void onDeviceDiscovered(@NonNull final ReaderDevice device) {
                if (devices.add(device)) {
                    devices.add(new ReaderDevice() {
                        @Override
                        public String getName() {
                            return "I am dummy";
                        }

                        @Override
                        public String getMac() {
                            return "dont connect";
                        }
                    });
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

    public Completable checkPermissions() {
        if (!bluetoothConditionChecker.hasAllPermissions()) {
            return Completable.error(new MissingPermissionsError());
        }

        return Completable.complete();
    }

    public Completable enableBluetoothWhenNecessary() {
        if (!bluetoothConditionChecker.hasBleFeature()) {
            return Completable.error(new MissingBleFeatureError());
        }

        if (!bluetoothConditionChecker.isBluetoothEnabled()) {
            return Completable.fromAction(bluetoothAdapter::enable);
        }

        return Completable.complete();
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
                .doOnComplete(() -> Countdown.countdown(period, ignore -> true, ignore -> ignore)
                        .take(1)
                        .flatMapCompletable(ignore -> stopScan())
                        .subscribe()
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
