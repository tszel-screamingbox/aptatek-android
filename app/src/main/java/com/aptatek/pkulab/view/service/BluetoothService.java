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
import com.aptatek.pkulab.domain.model.reader.ConnectionEvent;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.injection.component.ApplicationComponent;
import com.aptatek.pkulab.injection.component.bluetooth.BluetoothComponent;
import com.aptatek.pkulab.injection.component.bluetooth.DaggerBluetoothComponent;
import com.aptatek.pkulab.injection.module.BluetoothServiceModule;
import com.aptatek.pkulab.injection.module.ServiceModule;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

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

        checkConnection();
    }

    private void checkConnection() {
        disposables.add(
                readerInteractor.getConnectedReader()
                        .toSingle()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                ignored -> {
                                    Timber.d("Device already connected: %s", ignored);
                                    showConnectedNotification();
                                    checkWorkflowSate();
                                },
                                error -> {
                                    Timber.d("Error during checkConnection: %s", error);

                                    if (error instanceof NoSuchElementException) {
                                        // no connected reader, start connection flow
                                        watchErrors();
                                        startScanAndAutoConnect();
                                    } else {
                                        // stop gracefully
                                        stopForeground(true);
                                        stopSelf();
                                    }
                                })
        );
    }

    private void showConnectedNotification() {
        disposables.add(
                readerInteractor.getBatteryLevel()
                        .repeatWhen(objectFlowable -> objectFlowable.delay(1, TimeUnit.MINUTES))
                        .takeUntil(readerInteractor.getReaderConnectionEvents()
                                .map(ConnectionEvent::getConnectionState)
                                .filter(state -> state == ConnectionState.DISCONNECTING)
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(percent -> {
                                    Timber.d("Battery update: %d", percent);
                                    notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.ConnectedToDeviceSilently(percent)));
                                }, error -> {
                                    Timber.d("Error while reading battery: %s", error);
                                    // stop service
                                    stopForeground(true);
                                    stopSelf();
                                },
                                () -> {
                                    Timber.d("Battery updates finished");
                                    // stop service
                                    stopForeground(true);
                                    stopSelf();
                                })
        );
    }

    private void checkWorkflowSate() {
        disposables.add(
                readerInteractor.getWorkflowState()
                        .filter(workflowState -> workflowState == WorkflowState.TEST_COMPLETE)
                        .take(1)
                        .flatMapCompletable(ignored -> readerInteractor.getTestProgress()
                                .filter(testProgress -> testProgress.getPercent() == 100)
                                .take(1)
                                .map(TestProgress::getStart)
                                .map(String::valueOf)
                                .flatMapSingle(testId -> readerInteractor.getResult(testId))
                                .flatMapCompletable(testResult -> readerInteractor.saveResult(testResult))
                        )
                        .repeatWhen(objectFlowable -> objectFlowable.flatMap(ignored -> readerInteractor.getWorkflowState().filter(workflowState -> workflowState == WorkflowState.TEST_RUNNING)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    Timber.d("checkWorkflowState: Test result successfully saved");
                                    notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.TestComplete()));
                                },
                                error -> {
                                    Timber.d("Error during checkWorkflowSate: %s", error);
                                    stopForeground(true);
                                    stopSelf();
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
                                        .flatMap(devices -> bluetoothInteractor.stopScan()
                                                    .andThen(Single.just(devices))
                                        )
                                )
                        )
                        .firstOrError()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(devices -> {
                                    switch (devices.size()) {
                                        case 1: {
                                            final ReaderDevice readerDevice = devices.get(0);
                                            Timber.d("startScanAndAutoConnect is connecting to device: %s", readerDevice.getMac());
                                            connectTo(readerDevice);
                                            break;
                                        }
                                        default: {
                                            Timber.d("startScanAndAutoConnect didn't find any reader device, stopping service...");
                                            // terminate service
                                            stopForeground(true);
                                            stopSelf();
                                            break;
                                        }
                                    }
                                },
                                error -> {
                                    Timber.d("Error during startScanAndAutoConnect: %s", error);

                                    if (error instanceof MissingPermissionsError) {
                                        final MissingPermissionsError missingPermissionsError = (MissingPermissionsError) error;
                                        // TODO what to do here?
                                        notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.BluetoothError(error.getMessage())));
                                        stopForeground(false);
                                        stopSelf();
                                    } else if (error instanceof MissingBleFeatureError) {
                                        // TODO fail gracefully?
                                        notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.BluetoothError(error.getMessage())));
                                        stopForeground(false);
                                        stopSelf();
                                    } else {
                                        stopForeground(true);
                                        stopSelf();
                                    }
                                })
        );
    }


    private void connectTo(@NonNull final ReaderDevice readerDevice) {
        disposables.add(
                readerInteractor.connect(readerDevice)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    Timber.d("connectTo: connected to %s", readerDevice.getMac());
                                    checkWorkflowSate();

                                    // TODO notification for silent and explicit connection
                                    final boolean silentFlow = true;
                                    if (silentFlow) {
                                        syncData();
                                    } else {
                                        notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.ConnectedToDeviceTestWorkflow()));
                                    }

                                },
                                error -> {
                                    Timber.d("error during connection to %s", readerDevice.getMac());

                                    stopForeground(true);
                                    stopSelf();
                                })
        );
    }

    private void syncData() {
        disposables.add(
                readerInteractor.getReaderConnectionEvents()
                .map(ConnectionEvent::getConnectionState)
                .filter(state -> state == ConnectionState.READY)
                .take(1)
                .flatMapSingle(ignored -> readerInteractor.syncResults())
                .singleOrError()
                .subscribe(ignored -> {
                    Timber.d("syncData successfully saved %d results", ignored.size());
                    showConnectedNotification();
                }, error -> {
                    Timber.d("syncData error: %s", error);
                    stopForeground(true);
                    stopSelf();
                })
        );
    }

    private void watchErrors() {
        disposables.add(bluetoothInteractor.getDiscoveryError()
                .subscribe(error -> {
                    Timber.d("Received error during discovery: %s", error);
                    notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.BluetoothError(error.getMessage())));
                    stopForeground(false);
                })
        );
    }
}
