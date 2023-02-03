package com.aptatek.pkulab.view.service;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.aptatek.pkulab.device.notifications.BluetoothNotificationFactory;
import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.MissingPermissionsError;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.test.ErrorInteractor;
import com.aptatek.pkulab.domain.interactor.test.ErrorModelConversionError;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.domain.model.reader.WorkflowStateUtils;
import com.aptatek.pkulab.injection.component.ApplicationComponent;
import com.aptatek.pkulab.injection.component.bluetooth.BluetoothComponent;
import com.aptatek.pkulab.injection.component.bluetooth.DaggerBluetoothComponent;
import com.aptatek.pkulab.injection.module.BluetoothServiceModule;
import com.aptatek.pkulab.injection.module.ServiceModule;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.error.ErrorModel;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

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

    @Inject
    ErrorInteractor errorInteractor;

    private int mode = -1;

    @Override
    public int onStartCommand(@Nullable final Intent intent, final int flags, final int startId) {
        if (intent != null) {
            mode = intent.getIntExtra(KEY_MODE, -1);

            return START_NOT_STICKY;
        }

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
    protected void startForeground() {
        final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.ConnectingToDevice());
        startForeground(notification.getId(), notification.getNotification());

        checkStateOrConnect();
    }

    private void checkStateOrConnect() {
        disposables.add(
                readerInteractor.getConnectedReader()
                        .toSingle()
                        .ignoreElement()
                        .subscribe(
                                this::onConnected,
                                error -> {
                                    if (error instanceof NoSuchElementException) {
                                        startConnect(0);
                                        return;
                                    }

                                    processError(error);
                                }
                        )
        );
    }

    private void startConnect(final int tryCount) {
        disposables.add(
                bluetoothInteractor.enableBluetoothWhenNecessary()
                        .andThen(bluetoothInteractor.stopScan())
                        .andThen(bluetoothInteractor.startScan(INITIAL_SCAN_PERIOD))
                        .andThen(Countdown.countdown(INITIAL_SCAN_PERIOD, tick -> tick >= 1, tick -> tick)
                                .flatMapSingle(ignored -> bluetoothInteractor.getDiscoveredDevices()
                                        .take(1)
                                        .firstOrError()
                                        .map(devices -> Ix.from(devices).toList())
                                )
                        )
                        .timeout(INITIAL_SCAN_PERIOD, TimeUnit.MILLISECONDS)
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
                        .subscribe(
                                this::onConnected,
                                this::processError
                        )
        );
    }

    private void onConnected() {
        watchErrorState();

        if (mode == MODE_READY) {
            waitUntilReady();
        } else if (mode == MODE_TEST_COMPLETE) {
            waitUntilTestComplete();
        } else {
            Timber.d("unknown startMode, shutting down...: %s", mode);
            shutdown();
        }
    }

    private void watchErrorState() {
        disposables.add(
                readerInteractor.getWorkflowState()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(wfs -> {
                            if (WorkflowStateUtils.isErrorState(wfs)) {
                                try {
                                    final ErrorModel errorModel = errorInteractor.createErrorModel(wfs, null, false);
                                    final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.WorkflowStateError(errorModel));
                                    notificationManager.notify(notification.getId(), notification.getNotification());
                                } catch (ErrorModelConversionError e) {
                                    // don't care
                                }
                            } else {
                                notificationManager.cancel(Constants.WORKFLOW_SATE_ERROR_NOTIFICATION_ID);
                            }
                        })
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

                                    shutdown();
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
                        .flatMapSingle(ignored ->
                                readerInteractor.getTestProgress()
                                        .filter(testProgress -> testProgress.getPercent() == 100)
                                        .take(1)
                                        .singleOrError()
                                        .map(TestProgress::getTestId)
                                        .map(String::valueOf)
                                        .flatMap(testId -> readerInteractor.getResult(testId, true))
                        )
                        .singleOrError()
                        .flatMap(testResult -> readerInteractor.saveResult(testResult).andThen(Single.just(testResult.getId())))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                testId -> {
                                    Timber.d("Test complete, result saved!");

                                    final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.TestComplete(testId));
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
            final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.ExplicitBtConnectionError());
            notificationManager.notify(notification.getId(), notification.getNotification());
        }

        shutdown();
    }

}
