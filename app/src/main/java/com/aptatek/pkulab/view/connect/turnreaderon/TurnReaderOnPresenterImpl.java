package com.aptatek.pkulab.view.connect.turnreaderon;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;

import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.LocationServiceDisabledError;
import com.aptatek.pkulab.domain.interactor.reader.MissingBleFeatureError;
import com.aptatek.pkulab.domain.interactor.reader.MissingPermissionsError;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.test.ErrorInteractor;
import com.aptatek.pkulab.domain.interactor.test.ErrorModelConversionError;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.readerconnection.DeniedPermission;
import com.aptatek.pkulab.domain.manager.analytic.events.readerconnection.ReaderConnectedFromTurnReaderOn;
import com.aptatek.pkulab.domain.manager.analytic.events.readerconnection.TurnReaderOnDisplayed;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.domain.model.reader.WorkflowStateUtils;
import com.aptatek.pkulab.view.connect.onboarding.turnon.TurnReaderOnConnectView;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.aptatek.pkulab.view.error.ErrorModel;
import com.aptatek.pkulab.view.main.continuetest.TurnReaderOnContinueTestView;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.turnreaderon.TurnReaderOnTestView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
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

    private final ErrorInteractor errorInteractor;
    private CompositeDisposable disposables = null;
    private Disposable workflowDisposable = null;
    private Disposable scanResultsDisposable = null;
    private Function<WorkflowState, Boolean> workflowStateHandler = workflowState -> false;
    private long connectStartedAtMs = 0L;
    private WeakReference<TurnReaderOnView> view;

    public TurnReaderOnPresenterImpl(final BluetoothInteractor bluetoothInteractor,
                                     final ReaderInteractor readerInteractor,
                                     final TestInteractor testInteractor,
                                     final IAnalyticsManager analyticsManager,
                                     final ErrorInteractor errorInteractor) {
        this.bluetoothInteractor = bluetoothInteractor;
        this.readerInteractor = readerInteractor;
        this.testInteractor = testInteractor;
        this.analyticsManager = analyticsManager;
        this.errorInteractor = errorInteractor;
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
                bluetoothInteractor.stopScan()
                        .andThen(readerInteractor.connect(readerDevice))
                        .subscribe(() -> {
                                    ifViewAttached(av -> av.showConnectedToToast(readerDevice.getName()));
                                    waitForWorkflowStateChange();
                                },
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
                                .andThen(bluetoothInteractor.checkLocationServicesEnabled(((TurnReaderOnFragment) view).getActivity()))
                                .andThen(readerInteractor.getConnectedReader()
                                        .toSingle())
                                .flatMap(ignored ->
                                        // we have a connected reader, lets check its workflow state...
                                        readerInteractor.getWorkflowState()
                                                .take(1)
                                                .singleOrError()
                                                .flatMap(wfs -> testInteractor.getLastScreen().map(screen -> new Pair<>(wfs, screen)))
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
                                            } else if (error instanceof LocationServiceDisabledError) {
                                                ifViewAttached(TurnReaderOnView::showEnableLocation);
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
                // wait 2 seconds to receive ready state
                readerInteractor.getReaderConnectionEvents()
                .filter(it -> it.getConnectionState() == ConnectionState.READY)
                .take(1)
                .timeout(2, TimeUnit.SECONDS)
                .ignoreElements()
                // ignore error, will try with something else
                .onErrorComplete()
                .andThen(Single.zip(
                        testInteractor.getLastScreen(),
                        // wait for workflow state 1 sec at most
                        readerInteractor.getWorkflowState().take(1).lastOrError().timeout(1, TimeUnit.SECONDS),
                        (testScreens, workflowState) -> new Pair<>(workflowState, testScreens)
                ))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleWorkflowState, error -> {
                    Timber.d("--- waitForWorkflowState error: %s", error);
                    waitForWorkflowStateChange();
                });
    }

    private void handleWorkflowState(final Pair<WorkflowState, TestScreens> pair) {
        Timber.d("--- handleWorkflowState: %s, %s", pair.first, pair.second);

        ifViewAttached(TurnReaderOnView::displaySelfCheckAnimation);
        final WorkflowState workflowState = pair.first;

        switch (workflowState) {
            case READY: {
                dismissTestNotifications();
                break;
            }
            case SELF_TEST:
            default: {
                try {
                    if (WorkflowStateUtils.isErrorState(workflowState)) {
                        try {
                            final ErrorModel errorModel = errorInteractor.createErrorModel(workflowState, null, TestScreens.ATTACH_CHAMBER.ordinal() < pair.second.ordinal());
                            ifViewAttached(av -> av.showErrorScreen(errorModel));
                        } catch (ErrorModelConversionError e) {
                            Timber.d("--- failed to convert error: %s", e);
                        }
                    } else {
                        final boolean handled = workflowStateHandler.apply(workflowState);
                        if (!handled) {
                            Timber.d("unhandled workflow state: %s", workflowState);
                            waitForWorkflowStateChange();
                        }

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
        if (view == null) return "null";

        final TurnReaderOnView attached = view.get();
        if (attached instanceof TurnReaderOnConnectView) {
            return "onboarding";
        } else if (attached instanceof TurnReaderOnContinueTestView) {
            return "unfinished_Test";
        } else if (attached instanceof TurnReaderOnTestView) {
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
                        .andThen(processReaderDevicesFlowableAfterDelay())
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

    private Single<Pair<String, List<ReaderDevice>>> processReaderDevicesFlowableAfterDelay() {
        return Completable.timer(1L, TimeUnit.SECONDS)
                .andThen(
                        Single.zip(
                                        readerInteractor.getLastConnectedName().toSingle().onErrorReturnItem("invalid"),
                                        bluetoothInteractor.getDiscoveredDevices()
                                                .take(1)
                                                .lastOrError()
                                                .map(devices -> Ix.from(devices).toList()),
                                        ((BiFunction<String, List<ReaderDevice>, Pair<String, List<ReaderDevice>>>) Pair::new))
                                .flatMap(pair -> {
                                    final List<ReaderDevice> devices = pair.second;
                                    if (pair.first.equals("invalid")) {
                                        // not paired with anything before, connect to the only available reader
                                        if (devices.size() == 1) {
                                            return bluetoothInteractor.stopScan()
                                                    .andThen(readerInteractor.connect(devices.get(0)))
                                                    .andThen(Single.just(pair));
                                        } else {
                                            return Single.just(pair);
                                        }
                                    } else {
                                        // connect to the only available device that is already paired
                                        if (devices.size() == 1 && Ix.from(devices).any(a -> a.getName().equals(pair.first)).first()) {
                                            return bluetoothInteractor.stopScan()
                                                    .andThen(readerInteractor.connect(devices.get(0)))
                                                    .andThen(Single.just(pair));
                                        } else {
                                            return Single.just(pair);
                                        }
                                    }
                                })
                );
    }

    private void handleDiscoveredDevices(final Pair<String, List<ReaderDevice>> pair) {
        final String lastConnected = pair.first;
        final List<ReaderDevice> foundDevices = pair.second;
        switch (foundDevices.size()) {
            case 0: {
                ifViewAttached(TurnReaderOnView::displayNoReaderAvailable);
                if (!lastConnected.equals("invalid")) {
                    ifViewAttached(av -> av.displayPairedReaderNotAvailable(lastConnected));
                }
                waitForScanResults();
                break;
            }
            case 1: {
                if (lastConnected.equals("invalid") || lastConnected.equals(foundDevices.get(0).getName())) {
                    ifViewAttached(av -> av.showConnectedToToast(foundDevices.get(0).getName()));
                    // at this point we should have already initiated connection
                    waitForWorkflowStateChange();
                } else {
                    ifViewAttached(av -> av.displayPairedReaderNotAvailable(lastConnected));
                }
                break;
            }
            default: {
                ifViewAttached(attachedView -> attachedView.displayReaderSelector(foundDevices));
                break;
            }
        }
    }

    private void waitForScanResults() {
        if (scanResultsDisposable != null && !scanResultsDisposable.isDisposed()) {
            scanResultsDisposable.dispose();
        }

        scanResultsDisposable =
                processReaderDevicesFlowableAfterDelay()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::handleDiscoveredDevices,
                                error -> Timber.d("Error in waitForScanResults: %s", error)
                        );
    }

    @Override
    public void evaluatePermissionResults(final List<PermissionResult> results) {
        final Boolean hasAllPermissions = Ix.from(results)
                .map(PermissionResult::getResult)
                .all(it -> it == PermissionChecker.PERMISSION_GRANTED)
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

    @Override
    public void disposeTest() {
        disposables.add(
                testInteractor.resetTest().subscribe()
        );
    }
}
