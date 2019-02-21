package com.aptatek.pkulab.view.test.turnreaderon;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenter;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenterImpl;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class TurnReaderOnTestPresenter extends TestBasePresenter<TurnReaderOnTestView> implements TurnReaderOnPresenter<TurnReaderOnTestView> {

    private final TurnReaderOnPresenterImpl wrapped;
    private final ReaderInteractor readerInteractor;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public TurnReaderOnTestPresenter(final ResourceInteractor resourceInteractor,
                                     final BluetoothInteractor bluetoothInteractor,
                                     final ReaderInteractor readerInteractor) {
        super(resourceInteractor);
        wrapped = new TurnReaderOnPresenterImpl(bluetoothInteractor, readerInteractor);
        this.readerInteractor = readerInteractor;
    }

    @Override
    public void attachView(final @NonNull TurnReaderOnTestView view) {
        super.attachView(view);
        wrapped.attachView(view);
    }

    @Override
    public void detachView() {
        wrapped.detachView();

        disposables.dispose();

        super.detachView();
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> attachedView.setNextButtonVisible(false));
    }

    @Override
    public void onResumed() {
        wrapped.onResumed();
    }

    @Override
    public void onPaused() {
        wrapped.onPaused();
    }

    @Override
    public void connectTo(@NonNull final ReaderDevice readerDevice) {
        wrapped.connectTo(readerDevice);
    }

    @Override
    public void evaluatePermissionResults(final List<PermissionResult> results) {
        wrapped.evaluatePermissionResults(results);
    }

    @Override
    public void checkPermissions() {
        wrapped.checkPermissions();
    }

    public void getBatteryLevel() {
        disposables.add(
                readerInteractor.getReaderConnectionEvents()
                .filter(connectionEvent -> connectionEvent.getConnectionState() == ConnectionState.READY)
                .take(1)
                .flatMapSingle(ignored -> readerInteractor.getBatteryLevel())
                .subscribe(batteryPercent -> ifViewAttached(attachedView -> {
                    attachedView.setBatteryIndicatorVisible(true);
                    attachedView.setBatteryPercentage(batteryPercent);
                }))
        );
    }

    public void tmpSyncData() {
        disposables.add(
                readerInteractor.syncResults()
                        .subscribe(
                                ignored -> ifViewAttached(TurnReaderOnTestView::tmpProceed),
                                error -> Timber.d("Error while running syncResults: %s", error)
                        )
        );
    }

}
