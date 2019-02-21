package com.aptatek.pkulab.domain.interactor.reader;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.error.DeviceDiscoveryError;
import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.domain.manager.reader.BluetoothAdapter;
import com.aptatek.pkulab.domain.manager.reader.BluetoothConditionChecker;
import com.aptatek.pkulab.domain.manager.reader.BluetoothScanner;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;

import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class BluetoothInteractor {

    private static final long DEFAULT_SCAN_PERIOD_IN_MS = 60 * 1000L;

    private final BluetoothScanner bluetoothScanner;
    private final BluetoothConditionChecker bluetoothConditionChecker;
    private final BluetoothAdapter bluetoothAdapter;

    @Inject
    public BluetoothInteractor(final BluetoothScanner bluetoothScanner,
                               final BluetoothConditionChecker bluetoothConditionChecker,
                               final BluetoothAdapter bluetoothAdapter) {
        this.bluetoothScanner = bluetoothScanner;
        this.bluetoothConditionChecker = bluetoothConditionChecker;
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public Completable checkPermissions(final Activity activity) {
        if (!bluetoothConditionChecker.hasAllPermissions()) {
            return Completable.error(new MissingPermissionsError(bluetoothConditionChecker.shouldShowRationale(activity), bluetoothConditionChecker.getMissingPermissions()));
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
        return bluetoothScanner.isScanning()
                .take(1)
                .flatMapCompletable(scanning -> {
                    if (scanning) {
                        return Completable.complete();
                    }

                    return bluetoothScanner.startScan()
                            .doOnComplete(() -> Countdown.countdown(period, ignore -> true, ignore -> ignore)
                                    .take(1)
                                    .flatMapCompletable(ignore -> stopScan())
                                    .subscribe()
                            ).subscribeOn(Schedulers.computation());
                });
    }


    @NonNull
    public Completable startScan() {
        return startScan(DEFAULT_SCAN_PERIOD_IN_MS);
    }

    @NonNull
    public Completable stopScan() {
        return bluetoothScanner.stopScan()
                .subscribeOn(Schedulers.computation());
    }

    @NonNull
    public Flowable<Boolean> isScanning() {
        return bluetoothScanner.isScanning();
    }

    @NonNull
    public Flowable<Set<ReaderDevice>> getDiscoveredDevices() {
        return bluetoothScanner.getDiscoveredDevices();
    }

    @NonNull
    public Flowable<DeviceDiscoveryError> getDiscoveryError() {
        return bluetoothScanner.getDiscoveryErrors();
    }

}
