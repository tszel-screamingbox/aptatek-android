package com.aptatek.pkulab.view.connect.turnreaderon;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;

import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.MissingBleFeatureError;
import com.aptatek.pkulab.domain.interactor.reader.MissingPermissionsError;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.readerconnection.DeniedPermission;
import com.aptatek.pkulab.domain.manager.analytic.events.readerconnection.ReaderConnectedFromTurnReaderOn;
import com.aptatek.pkulab.domain.manager.analytic.events.readerconnection.TurnReaderOnDisplayed;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.connect.onboarding.turnon.TurnReaderOnConnectView;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.aptatek.pkulab.view.main.continuetest.TurnReaderOnContinueTestView;
import com.aptatek.pkulab.view.test.turnreaderon.TurnReaderOnTestView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ix.Ix;
import timber.log.Timber;

public class TurnReaderOnPresenterImpl extends MvpBasePresenter<TurnReaderOnView> implements TurnReaderOnPresenter<TurnReaderOnView> {

    private static final long INITIAL_SCAN_PERIOD = 5000L;

    private final BluetoothInteractor bluetoothInteractor;
    private final ReaderInteractor readerInteractor;
    private final TestInteractor testInteractor;
    private final IAnalyticsManager analyticsManager;
    private CompositeDisposable disposables = null;
    private Disposable workflowDisposable = null;
    private Disposable scanResultsDisposable = null;
    private Function<WorkflowState, Boolean> workflowStateHandler = workflowState -> false;
    private long connectStartedAtMs = 0L;
    private WeakReference<TurnReaderOnView> view;

    public TurnReaderOnPresenterImpl(final BluetoothInteractor bluetoothInteractor,
                                     final ReaderInteractor readerInteractor,
                                     final TestInteractor testInteractor,
                                     final IAnalyticsManager analyticsManager) {
        this.bluetoothInteractor = bluetoothInteractor;
        this.readerInteractor = readerInteractor;
        this.testInteractor = testInteractor;
        this.analyticsManager = analyticsManager;
    }

    @Override
    public void attachView(@NotNull final TurnReaderOnView view) {
        super.attachView(view);

        this.view = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        this.view = null;

        super.detachView();
    }

    @Override
    public void onResumed() {
        disposeSubscription();
        disposables = new CompositeDisposable();

        checkPermissions();

        // TODO add error handling
    }

    @Override
    public void onPaused() {
        disposables.add(bluetoothInteractor.stopScan()
                .subscribe(
                        () -> {
                            Timber.d("Stopped bt scan");
                            disposeSubscription();
                        },
                        Timber::e
                ));
    }

    private void disposeSubscription() {
        if (disposables != null) {
            disposables.dispose();
        }
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
        disposables.add(
                readerInteractor.disconnect()
                        .onErrorComplete()
                        .subscribe(this::checkPermissions,
                                Timber::e)
        );
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
        if (workflowDisposable != null && !workflowDisposable.isDisposed()) {
            workflowDisposable.dispose();
        }

        workflowDisposable =
                Countdown.countdown(5000L, tick -> tick >= 1, tick -> tick)
                        .take(1)
                        .flatMap(ignored -> readerInteractor.getWorkflowState())
                        .take(1)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleWorkflowState, Timber::e);
    }

    private void handleWorkflowState(final WorkflowState workflowState) {
        ifViewAttached(TurnReaderOnView::displaySelfCheckAnimation);

        switch (workflowState) {
            case READY: {
                dismissTestNotifications();
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

                break;
            }
        }
    }

    private void dismissTestNotifications() {
        disposables.add(
                testInteractor.cancelTestNotifications()
                        .andThen(
                                Single.zip(
                                        readerInteractor.getConnectedReader().toSingle(),
                                        readerInteractor.getBatteryLevel(),
                                        Pair::new
                                ))
                        .subscribe(
                                pair -> {
                                    analyticsManager.logEvent(new ReaderConnectedFromTurnReaderOn(pair.first.getSerial(), pair.first.getFirmwareVersion(), getStepId(), Math.abs(System.currentTimeMillis() - connectStartedAtMs), pair.second));

                                    Timber.d("Cancelled all test notifications");
                                    ifViewAttached(TurnReaderOnView::onSelfCheckComplete);
                                },
                                Timber::e
                        )
        );
    }

    private String getStepId() {
        final TurnReaderOnView attached = view.get();
        if (view instanceof TurnReaderOnConnectView) {
            return "onboarding";
        } else if (view instanceof TurnReaderOnContinueTestView) {
            return "unfinished_Test";
        } else if (view instanceof TurnReaderOnTestView) {
            return "final_phe_testing";
        } else {
            return "null";
        }
    }

    private void startConnectionFlow() {
        disposables.add(
                bluetoothInteractor.stopScan()
                        .andThen(Completable.fromAction(() -> connectStartedAtMs = System.currentTimeMillis()))
                        .andThen(bluetoothInteractor.startScan())
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
        if (scanResultsDisposable != null && !scanResultsDisposable.isDisposed()) {
            scanResultsDisposable.dispose();
        }

        scanResultsDisposable =
                processReaderDevicesFlowable()
                        .subscribe(
                                this::handleDiscoveredDevices,
                                error -> Timber.d("Error in waitForScanResults: %s", error)
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
            analyticsManager.logEvent(new DeniedPermission());
            ifViewAttached(TurnReaderOnView::displayMissingPermissions);
        }
    }

    public void setWorkflowStateHandler(final Function<WorkflowState, Boolean> workflowStateHandler) {
        this.workflowStateHandler = workflowStateHandler;
    }

    @Override
    public void logScreenDisplayed() {
        analyticsManager.logEvent(new TurnReaderOnDisplayed());
    }
}
