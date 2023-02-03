package com.aptatek.pkulab.view.service;

import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.aptatek.pkulab.AptatekApplication;
import com.aptatek.pkulab.BuildConfig;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.device.notifications.BluetoothNotificationFactory;
import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.test.ErrorInteractor;
import com.aptatek.pkulab.domain.interactor.test.ErrorModelConversionError;
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
import com.aptatek.pkulab.util.Constants;

import java.util.NoSuchElementException;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ix.Ix;
import timber.log.Timber;

public class BluetoothService extends BaseForegroundService {

    private static final long INITIAL_SCAN_PERIOD = 5000L;

    private static BluetoothService instance = null;

    public static boolean isServiceRunning() {
        return instance != null;
    }

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

    @Inject
    ErrorInteractor errorInteractor;

    private Disposable testCompleteDisposable;
    private Disposable errorStateDisposable;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    @Override
    public void onDestroy() {
        instance = null;

        disposeTestComplete();
        disposeErrorState();

        super.onDestroy();
    }

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
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    protected void startForeground() {
        if (TextUtils.isEmpty(preferenceManager.getPairedDevice()) || BuildConfig.FLAVOR.equals("mock")) {
            stopSelf();
            return;
        }

        final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.ConnectingToDevice());
        startForeground(notification.getId(), notification.getNotification());

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
                                    checkWorkflowErrorState();
                                    checkTestComplete();
                                },
                                error -> {
                                    Timber.d("Error during checkConnection: %s", error);

                                    if (error instanceof NoSuchElementException) {
                                        // no connected reader, start connection flow
                                        startScanAndAutoConnect();
                                    } else {
                                        // stop gracefully
                                        failSilently();
                                    }
                                })
        );
    }

    private void showConnectedNotification() {
        disposables.add(
                readerInteractor.batteryLevelUpdates()
                        .takeUntil(readerInteractor.getReaderConnectionEvents()
                                .map(ConnectionEvent::getConnectionState)
                                .filter(state -> state == ConnectionState.DISCONNECTING || state == ConnectionState.DISCONNECTED)
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(percent -> {
                                    Timber.d("Battery update: %d", percent);
                                    final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.ConnectedToDeviceSilently(percent));
                                    notificationManager.notify(notification.getId(), notification.getNotification());
                                }, error -> {
                                    Timber.d("Error while reading battery: %s", error);
                                    // stop service
                                    failSilently();
                                },
                                () -> {
                                    Timber.d("Battery updates finished");
                                    // stop service
                                    failSilently();
                                })
        );
    }

    private void checkWorkflowErrorState() {
        disposeErrorState();

        errorStateDisposable = readerInteractor.getWorkflowState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((wfs -> Flowable.fromCallable(() -> {
                    try {
                        return errorInteractor.createErrorModel(wfs, null, false);
                    } catch (ErrorModelConversionError e) {
                        throw new Exception(e);
                    }
                })))
                .subscribe(errorModel -> {
                            if (!AptatekApplication.get(this).isInForeground()) {
                                final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.WorkflowStateError(errorModel));
                                notificationManager.notify(notification.getId(), notification.getNotification());
                            } else {
                                notificationManager.cancel(Constants.WORKFLOW_SATE_ERROR_NOTIFICATION_ID);
                            }
                        },
                        error -> {
                            notificationManager.cancel(Constants.WORKFLOW_SATE_ERROR_NOTIFICATION_ID);
                        });
    }

    private void checkTestComplete() {
        disposeTestComplete();

        testCompleteDisposable =
                readerInteractor.getWorkflowState()
                        .filter(workflowState -> workflowState == WorkflowState.TEST_COMPLETE)
                        .take(1)
                        .flatMapSingle(ignored -> readerInteractor.getTestProgress()
                                .filter(testProgress -> testProgress.getPercent() == 100)
                                .take(1)
                                .singleOrError()
                                .map(TestProgress::getTestId)
                                .map(String::valueOf)
                                .flatMap(testId -> {
                                            if (AptatekApplication.get(getApplicationContext()).isInForeground()) {
                                                throw new AppInForegroundException();
                                            }
                                            return readerInteractor.getResult(testId, true);
                                        }
                                )
                        )
                        // should only trigger checkTestComplete if a new READING_CASSETE has been seen
                        // .takeUntil((a) -> a.getId() != null)
                        .singleOrError()
                        .flatMap(testResult ->
                                readerInteractor.saveResult(testResult)
                                        .andThen(Single.just(testResult.getId()))
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(testId -> {
                                    Timber.d("checkWorkflowState: Test result successfully saved");

                                    if (!AptatekApplication.get(this).isInForeground()) {
                                        final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.TestComplete(testId));
                                        notificationManager.notify(notification.getId(), notification.getNotification());
                                    }

                                    checkTestComplete();
                                },
                                error -> {
                                    if (error instanceof AppInForegroundException) {
                                        checkTestComplete();
                                    } else {
                                        Timber.d("Error during checkTestComplete: %s", error);
                                        failSilently();
                                    }
                                }
                        );
    }

    private void disposeTestComplete() {
        if (testCompleteDisposable != null && !testCompleteDisposable.isDisposed()) {
            testCompleteDisposable.dispose();
        }
    }

    private void disposeErrorState() {
        if (errorStateDisposable != null && !errorStateDisposable.isDisposed()) {
            errorStateDisposable.dispose();
        }
    }

    private void startScanAndAutoConnect() {
        disposables.add(
                bluetoothInteractor.enableBluetoothWhenNecessary()
                        .andThen(bluetoothInteractor.startScan())
                        .andThen(Countdown.countdown(INITIAL_SCAN_PERIOD, tick -> tick >= 1, tick -> tick)
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
                                        case 1: {
                                            final ReaderDevice readerDevice = devices.get(0);
                                            Timber.d("startScanAndAutoConnect is connecting to device: %s", readerDevice.getMac());
                                            connectTo(readerDevice);
                                            break;
                                        }
                                        default: {
                                            Timber.d("startScanAndAutoConnect didn't find any reader device, stopping service...");
                                            // terminate service
                                            failSilently();
                                            break;
                                        }
                                    }
                                },
                                error -> {
                                    Timber.d("Error during startScanAndAutoConnect: %s", error);
                                    failSilently();
                                })
        );
    }

    private void failSilently() {
        stopForeground(true);
        stopSelf();
    }

    private void connectTo(@NonNull final ReaderDevice readerDevice) {
        disposables.add(
                readerInteractor.connect(readerDevice)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    Timber.d("connectTo: connected to %s", readerDevice.getMac());
                                    checkTestComplete();
                                    syncData();
                                },
                                error -> {
                                    Timber.d("error during connection to %s", readerDevice.getMac());

                                    failSilently();
                                })
        );
    }

    private void syncData() {
        disposables.add(
                readerInteractor.getReaderConnectionEvents()
                        .map(ConnectionEvent::getConnectionState)
                        .filter(state -> state == ConnectionState.READY)
                        .take(1)
                        .doOnNext(ignored -> {
                            Timber.d("syncData: showing notification");
                            final BluetoothNotificationFactory.DisplayNotification notification = bluetoothNotificationFactory.createNotification(new BluetoothNotificationFactory.SyncingData());
                            notificationManager.notify(notification.getId(), notification.getNotification());
                        })
                        .flatMapSingle(ignored -> readerInteractor.syncResultsAfterLatest())
                        .singleOrError()
                        .subscribe(ignored -> {
                            Timber.d("syncData successfully saved %d results", ignored.size());
                            showConnectedNotification();
                        }, error -> {
                            Timber.d("syncData error: %s", error);
                            failSilently();
                        })
        );
    }

    private class AppInForegroundException extends Exception {

    }
}
