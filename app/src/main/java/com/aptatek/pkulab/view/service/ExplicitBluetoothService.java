package com.aptatek.pkulab.view.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;

import com.aptatek.pkulab.device.notifications.BluetoothNotificationFactory;
import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.MissingPermissionsError;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.injection.component.ApplicationComponent;
import com.aptatek.pkulab.injection.component.bluetooth.BluetoothComponent;
import com.aptatek.pkulab.injection.component.bluetooth.DaggerBluetoothComponent;
import com.aptatek.pkulab.injection.module.BluetoothServiceModule;
import com.aptatek.pkulab.injection.module.ServiceModule;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ix.Ix;
import timber.log.Timber;

public class ExplicitBluetoothService extends BaseForegroundService {

    private static final String KEY_MODE = "pkulab.explicitbtservice.mode";
    private static final long INITIAL_SCAN_PERIOD = 5000L;
    private static final int MAX_CONNECT_RETRY = 5;

    private static final int MODE_READY = 1;
    private static final int MODE_TEST_COMPLETE = 2;

    public static Intent createForDeviceReady(final Context context) {
        return createIntentForMode(context, MODE_READY);
    }

    public static Intent createForTestComplete(final Context context) {
        return createIntentForMode(context, MODE_TEST_COMPLETE);
    }

    @NonNull
    private static Intent createIntentForMode(Context context, int mode) {
        final Intent intent = new Intent(context, ExplicitBluetoothService.class);
        intent.putExtra(KEY_MODE, mode);
        return intent;
    }

    @Inject
    BluetoothInteractor bluetoothInteractor;

    @Inject
    ReaderInteractor readerInteractor;

    @Inject
    NotificationManagerCompat notificationManager;

    @Inject
    BluetoothNotificationFactory bluetoothNotificationFactory;

    private int mode = -1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mode = intent.getIntExtra(KEY_MODE, -1);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void injectService(final ApplicationComponent component) {
        final BluetoothComponent bluetoothComponent = DaggerBluetoothComponent.builder()
                .applicationComponent(component)
                .serviceModule(new ServiceModule(this))
                .bluetoothServiceModule(new BluetoothServiceModule())
                .build();
        bluetoothComponent.inject(this);
    }

    @Override
    protected Single<Boolean> shouldStart() {
        return readerInteractor.getConnectedReader()
                .toSingle()
                .map(reader -> reader == null)
                .onErrorReturnItem(true);
    }

    @Override
    protected void startForeground() {
        final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.ConnectingToDevice());
        startForeground(notification.getId(), notification.getNotification());

        startConnect(0);
    }

    private void startConnect(final int tryCount) {
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
                        ).andThen(Countdown.countdown(INITIAL_SCAN_PERIOD, tick -> tick >= 1, tick -> tick)
                        .flatMapSingle(ignored -> bluetoothInteractor.getDiscoveredDevices()
                                .take(1)
                                .firstOrError()
                                .map(devices -> Ix.from(devices).toList())
                        )
                )
                        .firstOrError()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(devices -> {
                                    switch (devices.size()) {
                                        case 0: {
                                            if (tryCount < MAX_CONNECT_RETRY) {
                                                Timber.d("startConnect no device found, keep trying...");
                                                startConnect(tryCount + 1);
                                            } else {
                                                Timber.d("startConnect has reached maximal try count, shutting down service...");

                                                final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.BluetoothError("Cannot connect to reader"));
                                                notificationManager.notify(notification.getId(), notification.getNotification());

                                                shutdown();
                                            }
                                            break;
                                        }
                                        case 1: {
                                            final ReaderDevice readerDevice = devices.get(0);
                                            Timber.d("startConnect is connecting to device: %s", readerDevice.getMac());

                                            connectTo(readerDevice);

                                            break;
                                        }
                                        default: {
                                            Timber.d("startConnect found multiple reader devices: %d", devices.size());

                                            final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.MultipleReadersDiscovered());
                                            notificationManager.notify(notification.getId(), notification.getNotification());

                                            shutdown();

                                            break;
                                        }
                                    }
                                },
                                this::processError
                        )
        );
    }

    private void connectTo(@NonNull final ReaderDevice readerDevice) {
        disposables.add(
                readerInteractor.connect(readerDevice)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    Timber.d("connectTo: connected to %s", readerDevice.getMac());
                                    if (mode == MODE_READY) {
                                        waitUntilReady();
                                    } else if (mode == MODE_TEST_COMPLETE) {
                                        waitUntilTestComplete();
                                    } else {
                                        Timber.d("unknown startMode, shutting down...: %s", mode);
                                        shutdown();
                                    }
                                },
                                this::processError
                        )
        );
    }

    private void waitUntilReady() {
        disposables.add(
                readerInteractor.getWorkflowState()
                        .filter(workflowState -> workflowState == WorkflowState.READY)
                        .take(1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                ignored -> {
                                    final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.ConnectedToDeviceTestWorkflow());
                                    notificationManager.notify(notification.getId(), notification.getNotification());
                                },
                                this::processError
                        )
        );
    }

    private void waitUntilTestComplete() {
        disposables.add(
                readerInteractor.getWorkflowState()
                        .filter(workflowState -> workflowState == WorkflowState.TEST_COMPLETE)
                        .take(1)
                        .flatMapCompletable(ignored ->
                                readerInteractor.getTestProgress()
                                        .filter(testProgress -> testProgress.getPercent() == 100)
                                        .take(1)
                                        .map(TestProgress::getStart)
                                        .map(String::valueOf)
                                        .flatMapSingle(readerInteractor::getResult)
                                        .flatMapCompletable(readerInteractor::saveResult)
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    Timber.d("Test complete, result saved!");

                                    final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.TestComplete());
                                    notificationManager.notify(notification.getId(), notification.getNotification());

                                    shutdown();
                                },
                                this::processError
                        )
        );
    }

    private void shutdown() {
        disposables.add(
                bluetoothInteractor.stopScan()
                        .subscribe(() -> {
                                    stopForeground(true);
                                    stopSelf();
                                },
                                Timber::e)
        );
    }

    private void processError(final Throwable throwable) {
        Timber.e(throwable);

        if (throwable instanceof MissingPermissionsError) {
            final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.MissingPermissionError());
            notificationManager.notify(notification.getId(), notification.getNotification());
        } else {
            final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.BluetoothError(throwable.getMessage()));
            notificationManager.notify(notification.getId(), notification.getNotification());
        }

        shutdown();
    }

}
