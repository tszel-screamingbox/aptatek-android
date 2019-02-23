package com.aptatek.pkulab.view.connect.onboarding.turnon;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenter;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenterImpl;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class TurnReaderOnConnectPresenter extends MvpBasePresenter<TurnReaderOnConnectView> implements TurnReaderOnPresenter<TurnReaderOnConnectView> {

    private final TurnReaderOnPresenterImpl wrapped;
    private final ReaderInteractor readerInteractor;
    private Disposable disposable;

    @Inject
    public TurnReaderOnConnectPresenter(final BluetoothInteractor bluetoothInteractor,
                                        final ReaderInteractor readerInteractor) {
        wrapped = new TurnReaderOnPresenterImpl(bluetoothInteractor, readerInteractor);
        this.readerInteractor = readerInteractor;
    }

    @Override
    public void attachView(@NonNull final TurnReaderOnConnectView view) {
        wrapped.attachView(view);
        super.attachView(view);
    }

    @Override
    public void detachView() {
        wrapped.detachView();
        super.detachView();

        disposeDisposable();
    }

    @Override
    public void checkPermissions() {
        wrapped.checkPermissions();
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
    public void connectTo(final @NonNull ReaderDevice readerDevice) {
        wrapped.connectTo(readerDevice);
    }

    @Override
    public void evaluatePermissionResults(final List<PermissionResult> results) {
        wrapped.evaluatePermissionResults(results);
    }

    public void syncData() {
        disposeDisposable();
        disposable = readerInteractor.syncResults()
                .subscribe(
                        ignored -> ifViewAttached(TurnReaderOnConnectView::navigateToHome),
                        error -> Timber.d("Error while running syncResults: %s", error)
                );
    }

    private void disposeDisposable() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }
}