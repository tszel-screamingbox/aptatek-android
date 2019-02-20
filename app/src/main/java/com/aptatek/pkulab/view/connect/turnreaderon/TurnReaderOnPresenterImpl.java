package com.aptatek.pkulab.view.connect.turnreaderon;

import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;

import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.MissingBleFeatureError;
import com.aptatek.pkulab.domain.interactor.reader.MissingPermissionsError;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import ix.Ix;
import timber.log.Timber;

public class TurnReaderOnPresenterImpl extends MvpBasePresenter<TurnReaderOnView> implements TurnReaderOnPresenter<TurnReaderOnView> {

    private static final long INITIAL_SCAN_PERIOD = 5000L;

    private final BluetoothInteractor bluetoothInteractor;
    private final ReaderInteractor readerInteractor;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public TurnReaderOnPresenterImpl(final BluetoothInteractor bluetoothInteractor,
                                     final ReaderInteractor readerInteractor) {
        this.bluetoothInteractor = bluetoothInteractor;
        this.readerInteractor = readerInteractor;
    }

    @Override
    public void onResumed() {
        checkPermissions();

        disposables.add(
                bluetoothInteractor.getDiscoveredDevices()
                        .skip(INITIAL_SCAN_PERIOD, TimeUnit.MILLISECONDS)
                        .map(devices -> Ix.from(devices).toList())
                        .take(1)
                        .flatMapSingle(devices -> {
                            if (devices.size() == 1) {
                                return readerInteractor.connect(devices.get(0))
                                        .andThen(Single.just(devices));
                            }

                            return Single.just(devices);
                        })
                        .subscribe(devices -> ifViewAttached(attachedView -> {
                                    switch (devices.size()) {
                                        case 0: {
                                            // do nothing here
                                            break;
                                        }
                                        case 1: {
                                            attachedView.displaySelfCheckAnimation();
                                            break;
                                        }
                                        default: {
                                            attachedView.displayReaderSelector(devices);
                                            break;
                                        }
                                    }
                                }),
                                error -> Timber.d("Error during device discovery: %s", error)));

        disposables.add(
                readerInteractor.getWorkflowState()
                        .filter(workflowState -> workflowState == WorkflowState.READY || workflowState == WorkflowState.DEFAULT)
                        .subscribe(ignored -> ifViewAttached(TurnReaderOnView::onSelfCheckComplete))
        );

        // TODO add error handling
    }

    @Override
    public void onPaused() {
        // what to do?
    }

    @Override
    public void connectTo(final @NonNull ReaderDevice readerDevice) {
        disposables.add(readerInteractor.connect(readerDevice)
                .subscribe()
        );
    }

    @Override
    public void checkPermissions() {
        ifViewAttached(view ->
                disposables.add(
                        bluetoothInteractor.checkPermissions(((TurnReaderOnFragment) view).getActivity())
                                .andThen(bluetoothInteractor.enableBluetoothWhenNecessary())
                                .andThen(bluetoothInteractor.isScanning()
                                        .take(1)
                                        .flatMapCompletable(isScanning -> {
                                            if (isScanning) {
                                                return Completable.complete();
                                            }

                                            return bluetoothInteractor.startScan(Long.MAX_VALUE);
                                        })
                                        .andThen(Countdown.countdown(INITIAL_SCAN_PERIOD, (tick -> tick >= INITIAL_SCAN_PERIOD), (tick -> INITIAL_SCAN_PERIOD - tick))
                                                .take(1)
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
                                        ))
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(devices -> ifViewAttached(attachedView -> {
                                            switch (devices.size()) {
                                                case 0: {
                                                    attachedView.displayNoReaderAvailable();
                                                    break;
                                                }
                                                case 1: {
                                                    attachedView.displaySelfCheckAnimation();
                                                    break;
                                                }
                                                default: {
                                                    attachedView.displayReaderSelector(devices);
                                                    break;
                                                }
                                            }
                                        }),
                                        error -> {
                                            Timber.d("Error during connection: %s", error);

                                            if (error instanceof MissingPermissionsError) {
                                                final MissingPermissionsError missingPermissionsError = (MissingPermissionsError) error;
                                                ifViewAttached(attachedView ->
                                                        attachedView.requestPermissions(missingPermissionsError.getMissingPermissions())
                                                );
                                            } else if (error instanceof MissingBleFeatureError) {
                                                ifViewAttached(TurnReaderOnView::showDeviceNotSupportedDialog);
                                            }
                                        }
                                )
                )
        );
    }

    @Override
    public void evaluatePermissionResults(final List<PermissionResult> results) {
        final Boolean hasAllPermissions = Ix.from(results)
                .map(PermissionResult::getResult)
                .map(result -> result == PermissionChecker.PERMISSION_GRANTED)
                .scan((prev, current) -> prev && current)
                .single(false);

        if (hasAllPermissions) {
            checkPermissions();
        } else {
            ifViewAttached(TurnReaderOnView::displayMissingPermissions);
        }
    }

}
