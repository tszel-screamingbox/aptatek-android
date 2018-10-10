package com.aptatek.pkulab.view.connect.scan;

import android.content.Context;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.reader.TimeServerInteractor;
import com.aptatek.pkulab.domain.model.ReaderDevice;
import com.aptatek.pkulab.injection.qualifier.ActivityContext;
import com.aptatek.pkulab.view.connect.common.BaseConnectScreenPresenter;
import com.aptatek.pkulab.view.connect.scan.adapter.ScanDeviceAdapterItem;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import ix.Ix;
import timber.log.Timber;

public class ScanPresenter extends BaseConnectScreenPresenter<ScanView> {

    private final BluetoothInteractor bluetoothInteractor;
    private final ReaderInteractor readerInteractor;
    private final TimeServerInteractor timeServerInteractor;

    private Set<ReaderDevice> readerDevices;

    private CompositeDisposable disposables;

    @Inject
    public ScanPresenter(@ActivityContext final Context context,
                         final BluetoothInteractor bluetoothInteractor,
                         final ReaderInteractor readerInteractor,
                         final TimeServerInteractor timeServerInteractor) {
        super(context);
        this.bluetoothInteractor = bluetoothInteractor;
        this.readerInteractor = readerInteractor;
        this.timeServerInteractor = timeServerInteractor;
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
                    Timber.e(error);
                    ifViewAttached(attachedView -> attachedView.showErrorToast(error.getClass().getSimpleName() + ": " + error.getMessage()));

                    // TODO handle
                }));

        disposables.add(bluetoothInteractor.getDiscoveredDevices()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(devices -> {
                    readerDevices = devices;
                    final List<ScanDeviceAdapterItem> adapterItems = Ix.from(devices)
                            .map(device -> ScanDeviceAdapterItem.builder().setReaderDevice(device).build())
                            .toList();
                    ifViewAttached(attachedView -> attachedView.displayScanResults(adapterItems));
                }));

        disposables.add(readerInteractor.getReaderConnectionEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    Timber.d("event [%s]", event);

                    ifViewAttached(attachedView -> {
                        switch (event.getConnectionState()) {
                            case CONNECTING:
                            case BOUND:
                            case CONNECTED:
                            case BONDING_REQUIRED:
                            case DISCONNECTING: {
                                final List<ScanDeviceAdapterItem> adapterItems = Ix.from(readerDevices)
                                        .map(device -> ScanDeviceAdapterItem.builder()
                                                .setEnabled(false)
                                                .setConnectingToThis(device.equals(event.getDevice()))
                                                .setReaderDevice(event.getDevice())
                                                .build())
                                        .toList();

                                attachedView.displayScanResults(adapterItems);
                                break;
                            }

                            case READY: {
                                attachedView.showConnected(event.getDevice());
                                break;
                            }

                            case DISCONNECTED: {
                                final List<ScanDeviceAdapterItem> adapterItems = Ix.from(readerDevices)
                                        .map(device -> ScanDeviceAdapterItem.builder()
                                                .setReaderDevice(device)
                                                .build())
                                        .toList();

                                attachedView.displayScanResults(adapterItems);
                                break;
                            }
                            default: {
                                Timber.d("Unhandled state [%s]", event.getConnectionState());

                                break;
                            }

                        }
                    });
                }));

        disposables.add(readerInteractor.getReaderError()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(error -> {
                    Timber.e(error);
                    ifViewAttached(attachedView -> attachedView.showErrorToast(error.getClass().getSimpleName() + ": " + error.getMessage()));

                    // TODO handle error, reset connection state?
                }));
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

    public void connect(final @NonNull ReaderDevice readerDevice) {
        disposables.add(readerInteractor.connect(readerDevice)
                .doOnComplete(() -> Timber.d("Successfully connected to device"))
                .andThen(timeServerInteractor.startTimeServer())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Timber.d("Successfully started time server"),
                        Timber::e // TODO handle errors
                ));
    }
}
