package com.aptatek.pkulab.view.connect.turnreaderon;

import android.text.TextUtils;

import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.NoSuchElementException;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import ix.Ix;
import timber.log.Timber;

public class TurnReaderOnPresenter extends MvpBasePresenter<TurnReaderOnView> {

    public static final long INITIAL_SCAN_PERIOD = 5000L;
    private final BluetoothInteractor bluetoothInteractor;
    private final ReaderInteractor readerInteractor;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    TurnReaderOnPresenter(final BluetoothInteractor bluetoothInteractor,
                          final ReaderInteractor readerInteractor) {
        this.bluetoothInteractor = bluetoothInteractor;
        this.readerInteractor = readerInteractor;
    }

    public void onResumed() {
        disposables.add(
                bluetoothInteractor.isScanning()
                        .take(1)
                        .filter(scanning -> !scanning)
                        .flatMapCompletable(ignored -> bluetoothInteractor.startScan())
                        .subscribe()
        );

        disposables.add(
                Countdown.countdown(INITIAL_SCAN_PERIOD, (tick -> tick >= INITIAL_SCAN_PERIOD), (tick -> INITIAL_SCAN_PERIOD - tick))
                        .flatMap(ignored ->
                                bluetoothInteractor.getDiscoveredDevices()
                                        .map(Set::isEmpty)
                                        .filter(isEmpty -> isEmpty))
                        .subscribe(ignored -> ifViewAttached(TurnReaderOnView::displayNoReaderAvailable))
        );

        disposables.add(
                readerInteractor.getLastConnectedMac()
                        .flatMapCompletable(lastPairedMac -> // there is a previously paired device, wait for 5 sec to discover it
                                bluetoothInteractor.getDiscoveredDevices()
                                        .takeUntil(Countdown.countdown(INITIAL_SCAN_PERIOD, (tick -> tick >= INITIAL_SCAN_PERIOD), (tick -> INITIAL_SCAN_PERIOD - tick)))
                                        .filter(devices -> devices.size() == 1)
                                        .map(devices -> Ix.from(devices).first())
                                        .filter(firstReader -> firstReader.getMac().equals(lastPairedMac))
                                        .singleOrError()
                                        .flatMapCompletable(readerInteractor::connect))
                        .onErrorResumeNext(error -> {
                                    if (error instanceof NoSuchElementException) { // if there is no previously paired device, just let it go :(
                                        return Completable.complete();
                                    }

                                    return Completable.error(error);
                                }
                        )
                        .subscribe(() -> Timber.d("ok"),
                                error -> Timber.d("no luck brother %s", error))
        );

        disposables.add(
                readerInteractor.getLastConnectedMac()
                    .toSingle("nope")
                    .filter(mac -> TextUtils.equals(mac, "nope"))
                    .flatMapCompletable(ignored -> { // no previously connected device, wait for 5 secs, then 1) if there's only 1 Lumos Camera, connect to it 2) if there are multiple Lumos Cameras, show chooser
                        return Countdown.countdown(INITIAL_SCAN_PERIOD, (tick -> tick >= INITIAL_SCAN_PERIOD), (tick -> INITIAL_SCAN_PERIOD - tick))
                                .lastOrError()
                                .flatMapCompletable(whatever ->
                                        bluetoothInteractor.getDiscoveredDevices()
                                                .filter(devices -> devices.size() == 1)
                                                .take(1)
                                                .map(devices -> Ix.from(devices).first())
                                                .singleOrError()
                                                .flatMapCompletable(readerInteractor::connect)
                                );
                    })
                .subscribe(() -> Timber.d("ok"),
                        error -> Timber.d("no luck brother %s", error))
        );

        disposables.add(
                readerInteractor.getLastConnectedMac()
                    .toSingle("nope")
                    .filter(mac -> TextUtils.equals(mac, "nope"))
                    .flatMapSingle(ignored -> { // no previously connected device, wait for 5 secs, then 1) if there's only 1 Lumos Camera, connect to it 2) if there are multiple Lumos Cameras, show chooser
                        return Countdown.countdown(INITIAL_SCAN_PERIOD, (tick -> tick >= INITIAL_SCAN_PERIOD), (tick -> INITIAL_SCAN_PERIOD - tick))
                                .lastOrError()
                                .flatMap(whatever ->
                                        bluetoothInteractor.getDiscoveredDevices()
                                                .filter(devices -> devices.size() > 1)
                                                .take(1)
                                                .map(set -> Ix.from(set).toList())
                                                .singleOrError()
                                );
                    })
                .subscribe((discoveredDevices) -> ifViewAttached(attachedView -> attachedView.displayReaderSelector(discoveredDevices)),
                        error -> Timber.d("no luck brother %s", error))
        );

        disposables.add(
                readerInteractor.getReaderConnectionEvents()
                .filter(event -> event.getConnectionState() == ConnectionState.READY)
                .take(1)
                .repeatWhen(objectFlowable ->  // re-subscribe on disconnect
                        objectFlowable.flatMap(ignored ->
                                readerInteractor.getReaderConnectionEvents()
                                        .filter(event -> event.getConnectionState() == ConnectionState.DISCONNECTED)
                                .take(1))
                )
                .subscribe(ready -> ifViewAttached(TurnReaderOnView::displaySelfCheckAnimation))
        );

        disposables.add(
                readerInteractor.getWorkflowState()
                        .filter(workflowState -> workflowState == WorkflowState.READY)
                        .subscribe(ignored -> ifViewAttached(TurnReaderOnView::onSelfCheckComplete))
        );

        // TODO add error handling

    }

    public void onPaused() {

    }
}
