package com.aptatek.pkulab.view.service;

import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.device.notifications.BluetoothNotificationFactory;
import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.MissingBleFeatureError;
import com.aptatek.pkulab.domain.interactor.reader.MissingPermissionsError;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.injection.component.ApplicationComponent;
import com.aptatek.pkulab.injection.component.bluetooth.BluetoothComponent;
import com.aptatek.pkulab.injection.component.bluetooth.DaggerBluetoothComponent;
import com.aptatek.pkulab.injection.module.BluetoothServiceModule;
import com.aptatek.pkulab.injection.module.ServiceModule;

import java.util.NoSuchElementException;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ix.Ix;
import timber.log.Timber;

public class BluetoothService extends BaseForegroundService {

    private static final long INITIAL_SCAN_PERIOD = 5000L;

    private static final int BT_NOTIFICATION_ID = 258;

    @Inject
    PreferenceManager preferenceManager;

    @Inject
    BluetoothInteractor bluetoothInteractor;

    @Inject
    ReaderInteractor readerInteractor;

    @Inject
    NotificationManagerCompat notificationManager;

    @Inject
    BluetoothNotificationFactory bluetoothNotificationFactory;

    @Override
    protected void injectService(ApplicationComponent component) {
        final BluetoothComponent bluetoothComponent = DaggerBluetoothComponent.builder()
                .applicationComponent(component)
                .serviceModule(new ServiceModule(this))
                .bluetoothServiceModule(new BluetoothServiceModule())
                .build();
        bluetoothComponent.inject(this);
    }

    @Override
    protected Single<Boolean> shouldStart() {
        return Single.fromCallable(() -> !TextUtils.isEmpty(preferenceManager.getPairedDevice()));
    }

    @Override
    protected void startForeground() {
        startForeground(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.ConnectingToDevice()));

        startScanAndAutoConnect();
        watchErrors();
        watchReaderEvents();
    }

    private void checkConnection() {
        disposables.add(
                readerInteractor.getConnectedReader()
                        .toSingle()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                ignored -> {
                                    // TODO already connected, terminate??
                                },
                                error -> {
                                    if (error instanceof NoSuchElementException) {
                                        // no connected reader, start connection flow
                                        startScanAndAutoConnect();
                                    }
                                })
        );
    }

    private void startScanAndAutoConnect() {
        disposables.add(
                bluetoothInteractor.enableBluetoothWhenNecessary()
                        .andThen(bluetoothInteractor.isScanning()
                                .take(1)
                                .flatMapCompletable(isScanning -> {
                                    if (isScanning) {
                                        return Completable.complete();
                                    }

                                    return bluetoothInteractor.startScan(Long.MAX_VALUE);
                                })
                        )
                        .andThen(Countdown.countdown(INITIAL_SCAN_PERIOD, tick -> tick >= 1, tick -> tick)
                                .flatMapSingle(ignored -> bluetoothInteractor.getDiscoveredDevices()
                                        .take(1)
                                        .firstOrError()
                                        .map(devices -> Ix.from(devices).toList())
                                        .flatMap(devices -> {
                                            if (devices.size() == 1) {
                                                return readerInteractor.connect(devices.get(0))
                                                        .andThen(Single.just(devices));
                                            }

                                            return Single.just(devices);
                                        })
                                )
                        )
                        .firstOrError()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(devices -> {
                                    switch (devices.size()) {
                                        case 0: {
                                            // TODO now what?
                                            break;
                                        }
                                        case 1: {
                                            connectTo(devices.get(0));
                                            break;
                                        }
                                        default: {
                                            // TODO more than 1 device
                                            break;
                                        }
                                    }
                                },
                                error -> {
                                    Timber.d("Error during connection: %s", error);

                                    if (error instanceof MissingPermissionsError) {
                                        final MissingPermissionsError missingPermissionsError = (MissingPermissionsError) error;
                                        // TODO what to do here?
                                        notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.CommunicationError(error.getMessage())));
                                        stopForeground(false);
                                    } else if (error instanceof MissingBleFeatureError) {
                                        // TODO fail gracefully?
                                        notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.CommunicationError(error.getMessage())));
                                        stopForeground(false);
                                    } else {
                                        stopForeground(true);
                                    }
                                })
        );
    }


    private void connectTo(@NonNull final ReaderDevice readerDevice) {
        disposables.add(
                readerInteractor.connect(readerDevice)
                        .andThen(readerInteractor.getBatteryLevel())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(batteryLevel -> {
                                    Timber.d("connected to %s", readerDevice.getMac());
                                    // TODO notification for silent and explicit connection
                                    notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.ConnectedToDevice(readerDevice, batteryLevel)));

                                    // TODO terminate service or sync data?
                                    watchReaderEvents();
                                },
                                error -> {
                                    Timber.d("error during connection to %s", readerDevice.getMac());
                                    // TODO how to handle?
                                    stopForeground(true);
                                })
        );
    }

    private void watchReaderEvents() {
        disposables.add(readerInteractor.getReaderConnectionEvents()
                .distinctUntilChanged()
                .filter(connectionEvent -> connectionEvent.getConnectionState() == ConnectionState.READY)
                .doOnNext(readyEvent -> notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.SyncingData(readyEvent.getDevice()))))
                .flatMapSingle(ignored -> readerInteractor.syncResults())
                .take(1)
                .firstOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> {
                            Timber.d("Device sync complete: synced %d records", ignored.size());
                            // TODO terminate?
                        },
                        error -> {
                            Timber.d("Error while sync: %s", error);
                            // TODO handle error?
                        })
        );

        disposables.add(
                readerInteractor.getReaderConnectionEvents()
                        .filter(connectionEvent -> connectionEvent.getConnectionState() == ConnectionState.READY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(ignored ->
                                readerInteractor.getWorkflowState()
                                        .filter(workflowState -> workflowState == WorkflowState.TEST_RUNNING)
                                        .flatMap(workflowState -> readerInteractor.getConnectedReader()
                                                .toFlowable()
                                                .map(BluetoothNotificationFactory.RunningTest::new)
                                        )
                        ).subscribe(runningTest -> notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(runningTest)),
                        error -> {
                            Timber.d("error while listening workflow state: %s", error);
                        })
        );

        disposables.add(
                readerInteractor.getReaderConnectionEvents()
                        .filter(connectionEvent -> connectionEvent.getConnectionState() == ConnectionState.READY)
                        .take(1)
                        .flatMap(ignored -> readerInteractor.getWorkflowState()
                                .filter(workflowState -> workflowState == WorkflowState.TEST_COMPLETE)
                                .flatMap(workflowState -> readerInteractor.getConnectedReader()
                                        .toFlowable()
                                        .map(BluetoothNotificationFactory.TestComplete::new))
                        ).subscribe(testComplete -> notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(testComplete)))
        );
    }

    private void watchErrors() {
        disposables.add(bluetoothInteractor.getDiscoveryError()
                .subscribe(error -> {
                    Timber.d("Received error during discovery: %s", error);
                    notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.CommunicationError(error.getMessage())));
                    stopForeground(false);
                })
        );
    }
}
