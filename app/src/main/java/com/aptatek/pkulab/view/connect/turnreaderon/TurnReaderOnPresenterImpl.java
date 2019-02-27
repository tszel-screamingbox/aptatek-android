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
import java.util.NoSuchElementException;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ix.Ix;
import timber.log.Timber;

public class TurnReaderOnPresenterImpl extends MvpBasePresenter<TurnReaderOnView> implements TurnReaderOnPresenter<TurnReaderOnView> {

    private static final long INITIAL_SCAN_PERIOD = 5000L;

    private final BluetoothInteractor bluetoothInteractor;
    private final ReaderInteractor readerInteractor;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private Function<WorkflowState, Boolean> workflowStateHandler;

    public TurnReaderOnPresenterImpl(final BluetoothInteractor bluetoothInteractor,
                                     final ReaderInteractor readerInteractor) {
        this.bluetoothInteractor = bluetoothInteractor;
        this.readerInteractor = readerInteractor;
    }

    @Override
    public void detachView() {
        super.detachView();

        disposables.dispose();
    }

    @Override
    public void onResumed() {
        checkPermissions();

        // TODO add error handling
    }

    @Override
    public void onPaused() {
        // what to do?
        // TODO start explicit flow!
    }

    @Override
    public void connectTo(final @NonNull ReaderDevice readerDevice) {
        disposables.add(
                readerInteractor.connect(readerDevice)
                        .andThen(bluetoothInteractor.stopScan())
                        .subscribe(this::waitForWorkflowStateChange,
                                error -> {
                                    Timber.d("connectTo error: %s", error);

                                    resetFlow();
                                })
        );
    }

    private void resetFlow() {
        disposables.add(readerInteractor.disconnect().subscribe(this::checkPermissions));
    }

    @Override
    public void checkPermissions() {
        ifViewAttached(view ->
                disposables.add(
                        bluetoothInteractor.checkPermissions(((TurnReaderOnFragment) view).getActivity())
                                .andThen(bluetoothInteractor.enableBluetoothWhenNecessary())
                                .andThen(readerInteractor.getConnectedReader()
                                        .toSingle())
                                .flatMap(ignored ->
                                        // we have a connected reader, lets check its workflow state...
                                        readerInteractor.getWorkflowState()
                                                .take(1)
                                                .singleOrError()
                                )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        this::handleWorkflowState,
                                        error -> {
                                            Timber.d("Error checkPermission: %s", error);

                                            if (error instanceof MissingPermissionsError) {
                                                final MissingPermissionsError missingPermissionsError = (MissingPermissionsError) error;
                                                ifViewAttached(attachedView ->
                                                        attachedView.requestPermissions(missingPermissionsError.getMissingPermissions())
                                                );
                                            } else if (error instanceof MissingBleFeatureError) {
                                                ifViewAttached(TurnReaderOnView::showDeviceNotSupportedDialog);
                                            } else if (error instanceof NoSuchElementException) {
                                                // no connected reader
                                                startConnectionFlow();
                                            }
                                        }
                                )
                )
        );
    }

    private void waitForWorkflowStateChange() {
        disposables.add(
                Countdown.countdown(5000L, tick -> tick >= 1, tick -> tick)
                        .take(1)
                        .flatMap(ignored -> readerInteractor.getWorkflowState())
                        .take(1)
                        .subscribe(this::handleWorkflowState)
        );
    }

    private void handleWorkflowState(final WorkflowState workflowState) {
        switch (workflowState) {
            case READY: {
                ifViewAttached(TurnReaderOnView::onSelfCheckComplete);
                break;
            }
            case SELF_TEST:
            default: {
                try {
                    final boolean handled = workflowStateHandler.apply(workflowState);
                    if (!handled) {
                        Timber.d("unhandled workflow state: %s", workflowState);
                        waitForWorkflowStateChange();
                    }

                } catch (final Exception e) {
                    Timber.d("Unexpected error: %s", e);
                    waitForWorkflowStateChange();
                }

                ifViewAttached(TurnReaderOnView::displaySelfCheckAnimation);

                break;
            }
        }
    }

    private void startConnectionFlow() {
        disposables.add(
                bluetoothInteractor.isScanning()
                        .take(1)
                        .flatMapCompletable(isScanning -> {
                            if (isScanning) {
                                return Completable.complete();
                            }

                            return bluetoothInteractor.startScan(Long.MAX_VALUE);
                        })
                        .andThen(processReaderDevicesFlowable())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::handleDiscoveredDevices,
                                error -> {
                                    Timber.d("Error in startConnectionFlow: %s", error);

                                    resetFlow();
                                })
        );
    }

    private Flowable<List<ReaderDevice>> processReaderDevicesFlowable() {
        return Countdown.countdown(INITIAL_SCAN_PERIOD, tick -> tick >= 1, tick -> tick)
                .flatMapSingle(ignored -> bluetoothInteractor.getDiscoveredDevices()
                        .take(1)
                        .firstOrError()
                        .map(devices -> Ix.from(devices).toList())
                        .flatMap(devices -> {
                            if (devices.size() == 1) {
                                return readerInteractor.connect(devices.get(0))
                                        .andThen(bluetoothInteractor.stopScan())
                                        .andThen(Single.just(devices));
                            }

                            return Single.just(devices);
                        })
                );
    }

    private void handleDiscoveredDevices(final List<ReaderDevice> devices) {
        switch (devices.size()) {
            case 0: {
                ifViewAttached(TurnReaderOnView::displayNoReaderAvailable);
                waitForScanResults();
                break;
            }
            case 1: {
                waitForWorkflowStateChange();
                break;
            }
            default: {
                ifViewAttached(attachedView -> attachedView.displayReaderSelector(devices));
                break;
            }
        }
    }

    private void waitForScanResults() {
        disposables.add(
                processReaderDevicesFlowable()
                        .subscribe(
                                this::handleDiscoveredDevices,
                                error -> {
                                    Timber.d("Error in waitForScanResults: %s", error);
                                })
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

    public void setWorkflowStateHandler(final Function<WorkflowState, Boolean> workflowStateHandler) {
        this.workflowStateHandler = workflowStateHandler;
    }

}
