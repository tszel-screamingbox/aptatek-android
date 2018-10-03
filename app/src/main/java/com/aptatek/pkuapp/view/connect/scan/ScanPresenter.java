package com.aptatek.pkuapp.view.connect.scan;

import android.content.Context;
import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.error.DeviceDiscoveryError;
import com.aptatek.pkuapp.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkuapp.domain.manager.reader.BluetoothScanCallbacks;
import com.aptatek.pkuapp.domain.manager.reader.BluetoothScanner;
import com.aptatek.pkuapp.domain.model.ReaderDevice;
import com.aptatek.pkuapp.injection.qualifier.ActivityContext;
import com.aptatek.pkuapp.view.connect.common.BaseConnectScreenPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class ScanPresenter extends BaseConnectScreenPresenter<ScanView> {

    private final BluetoothInteractor bluetoothInteractor;

    private CompositeDisposable disposables;

    @Inject
    public ScanPresenter(@ActivityContext final Context context,
                         final BluetoothInteractor bluetoothInteractor) {
        super(context);
        this.bluetoothInteractor = bluetoothInteractor;
    }

    @Override
    public void attachView(ScanView view) {
        super.attachView(view);

        disposeSubscriptions();
        disposables = new CompositeDisposable();

        disposables.add(bluetoothInteractor.isScanning()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(scanning -> ifViewAttached(attachedView -> attachedView.showLoading(scanning))));

        disposables.add(bluetoothInteractor.getDiscoveryError()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(error -> {
                    // TODO handle
                    Timber.e(error);
                }));

        disposables.add(bluetoothInteractor.getDiscoveredDevices()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(devices -> ifViewAttached(attachedView -> attachedView.displayScanResults(devices))));
    }

    @Override
    public void detachView() {
        super.detachView();

        disposeSubscriptions();
    }

    private void disposeSubscriptions() {
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    @Override
    protected void onRequiredConditionsMet() {
        startScan();
    }

    @Override
    protected void onMissingPermissionsFound() {
        stopScan();

        // TODO handle this case
    }

    public void startScan() {
        disposables.add(bluetoothInteractor.startScan()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    public void stopScan() {
        disposables.add(bluetoothInteractor.stopScan()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }
}
