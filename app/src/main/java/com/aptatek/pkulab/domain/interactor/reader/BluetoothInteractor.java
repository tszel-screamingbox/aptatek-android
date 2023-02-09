package com.aptatek.pkulab.domain.interactor.reader;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.error.BluetoothDisabledError;
import com.aptatek.pkulab.domain.error.DeviceDiscoveryError;
import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.domain.manager.reader.BluetoothAdapter;
import com.aptatek.pkulab.domain.manager.reader.BluetoothConditionChecker;
import com.aptatek.pkulab.domain.manager.reader.BluetoothScanner;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class BluetoothInteractor {

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

    public Completable checkLocationServicesEnabled(Activity context) {
        final boolean isLocationEnabled;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            isLocationEnabled = locationManager.isLocationEnabled();
        } else {
            final ContentResolver contentResolver = context.getContentResolver();
            isLocationEnabled = Settings.Secure.getInt(contentResolver, Settings.Secure.LOCATION_MODE, 0) != Settings.Secure.LOCATION_MODE_OFF;
        }

        if (isLocationEnabled) {
            return Completable.complete();
        } else {
            return Completable.error(new LocationServiceDisabledError());
        }
    }

    public Completable enableBluetoothWhenNecessary() {
        if (!bluetoothConditionChecker.hasBleFeature()) {
            return Completable.error(new MissingBleFeatureError());
        }

        if (!bluetoothConditionChecker.isBluetoothEnabled()) {
            try {
                bluetoothAdapter.enable();
                return Completable.complete();
            } catch (BluetoothDisabledError error) {
                return Completable.error(error);
            }
        }

        return Completable.complete();
    }

    @NonNull
    public Completable startScan(final long period) {
        return bluetoothScanner.isScanning()
                .take(1)
                .lastOrError()
                .onErrorReturnItem(false)
                .flatMapCompletable(scanning -> {
                    if (scanning) {
                        return Completable.complete();
                    }

                    return bluetoothScanner.startScan()
                            .andThen(period == 0L ? Completable.complete() : Completable.timer(period, TimeUnit.MILLISECONDS).andThen(stopScan()))
                            .subscribeOn(Schedulers.computation());
                });
    }


    @NonNull
    public Completable startScan() {
        return startScan(0L);
    }

    @NonNull
    public Completable stopScan() {
        return bluetoothScanner.stopScan()
                .doOnError(e -> Timber.d("--- stopScan onError %s", e))
                .onErrorComplete()
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
