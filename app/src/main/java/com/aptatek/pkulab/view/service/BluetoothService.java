package com.aptatek.pkulab.view.service;

import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Pair;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.device.notifications.BluetoothNotificationFactory;
import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.injection.component.ApplicationComponent;
import com.aptatek.pkulab.injection.component.bluetooth.BluetoothComponent;
import com.aptatek.pkulab.injection.component.bluetooth.DaggerBluetoothComponent;
import com.aptatek.pkulab.injection.module.BluetoothServiceModule;
import com.aptatek.pkulab.injection.module.ServiceModule;
import com.aptatek.pkulab.injection.module.scan.ScanModule;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ix.Ix;
import timber.log.Timber;

public class BluetoothService extends BaseForegroundService {

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
                .scanModule(new ScanModule())
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

    private void startScanAndAutoConnect() {
        disposables.add(
                bluetoothInteractor.startScan()
                        .andThen(bluetoothInteractor.getDiscoveredDevices()
                                .filter(readerDevices -> !readerDevices.isEmpty())
                                .map(readerDevices -> Ix.from(readerDevices).first())
                                .flatMapCompletable(device -> readerInteractor.connect(device))
                        ).subscribe(
                        () -> Timber.d("Connected to device"),
                        throwable -> {
                            Timber.d("Something went wrong during service connection: %s", throwable);
                            notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.CommunicationError(throwable.getMessage())));
                            stopForeground(false);
                        }
                )
        );
    }

    private void watchReaderEvents() {
        disposables.add(readerInteractor.getReaderConnectionEvents()
                .filter(connectionEvent -> connectionEvent.getConnectionState() == ConnectionState.READY)
                .flatMap(readyEvent -> readerInteractor.syncResults()
                        .doOnSubscribe(ignored -> notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.SyncingData(readyEvent.getDevice(), 100))))
                        .toFlowable()
                        .take(1)
                        .flatMap(ignored -> readerInteractor.getBatteryLevel()
                                .map(level -> new Pair<>(readyEvent, level))
                                .repeatWhen(emitter -> emitter.delay(1, TimeUnit.MINUTES))
                                .takeUntil(readerInteractor.getReaderConnectionEvents()
                                        .filter(connectionEvent -> connectionEvent.getConnectionState() == ConnectionState.DISCONNECTED))
                        )
                )
                .subscribe(pair -> {
                    Timber.d("Device connected: %s, battery level: %d", pair.first.getDevice().getMac(), pair.second);
                    notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.ConnectedToDevice(pair.first.getDevice(), pair.second)));
                },
                error -> {
                    Timber.d("Error while sync and idle: %s", error);
                    // TODO handle error?
                })
        );

        disposables.add(readerInteractor.getReaderConnectionEvents()
                .filter(connectionEvent -> connectionEvent.getConnectionState() == ConnectionState.DISCONNECTED)
                .skip(1)
                .subscribe(ignore -> {
                    Timber.d("Device disconnected, stopping service...");
                    notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.DeviceDisconnected()));
                    stopForeground(false);
                })
        );

        disposables.add(
                readerInteractor.getReaderConnectionEvents()
                        .filter(connectionEvent -> connectionEvent.getConnectionState() == ConnectionState.READY)
                        .take(1)
                        .flatMap(ignored ->
                                readerInteractor.getWorkflowState()
                                        .filter(workflowState -> workflowState == WorkflowState.TEST_RUNNING)
                                        .flatMap(workflowState -> readerInteractor.getConnectedReader()
                                                .toFlowable()
                                                .map(BluetoothNotificationFactory.RunningTest::new)
                                        )
                        ).subscribe(runningTest -> notificationManager.notify(BT_NOTIFICATION_ID, bluetoothNotificationFactory.createNotification(runningTest)))
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
