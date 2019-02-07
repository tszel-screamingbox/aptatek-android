package com.aptatek.pkulab.view.connect.turnreaderon;

import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.MissingPermissionsError;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import ix.Ix;
import timber.log.Timber;

public class TurnReaderOnPresenterImpl extends MvpBasePresenter<TurnReaderOnView> implements TurnReaderOnPresenter<TurnReaderOnView> {

    private static final long INITIAL_SCAN_PERIOD = 5000L;

    private final BluetoothInteractor bluetoothInteractor;
    private final ReaderInteractor readerInteractor;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public TurnReaderOnPresenterImpl(final BluetoothInteractor bluetoothInteractor,
                              final ReaderInteractor readerInteractor) {
        this.bluetoothInteractor = bluetoothInteractor;
        this.readerInteractor = readerInteractor;
    }

    @Override
    public void onResumed() {
        disposables.add(
                bluetoothInteractor.checkPermissions()
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
                                    Timber.d("no luck bro: %s", error);
                                    
                                    if (error instanceof MissingPermissionsError) {
                                        ifViewAttached(TurnReaderOnView::displayMissingPermissions);
                                    }
                                }
                        )
        );

        disposables.add(
                bluetoothInteractor.getDiscoveredDevices()
                        .skip(INITIAL_SCAN_PERIOD, TimeUnit.MILLISECONDS)
                        .map(devices -> Ix.from(devices).toList())
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
                                error -> Timber.d("no luck bro: %s", error)));

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
}
