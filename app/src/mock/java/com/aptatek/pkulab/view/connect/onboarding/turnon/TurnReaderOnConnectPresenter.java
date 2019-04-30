package com.aptatek.pkulab.view.connect.onboarding.turnon;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class TurnReaderOnConnectPresenter extends MvpBasePresenter<TurnReaderOnConnectView> implements TurnReaderOnPresenter<TurnReaderOnConnectView> {

    private final ReaderInteractor readerInteractor;
    private Disposable disposable;

    @Inject
    public TurnReaderOnConnectPresenter(final ReaderInteractor readerInteractor) {
        this.readerInteractor = readerInteractor;
    }

    @Override
    public void detachView() {
        disposeDisposable();
        super.detachView();
    }

    @Override
    public void checkPermissions() {
        // no need to do that in MOCK
    }

    @Override
    public void onResumed() {
        ifViewAttached(TurnReaderOnConnectView::displaySkipButton);
    }

    @Override
    public void onPaused() {
        // do nothing in MOCK
    }

    @Override
    public void connectTo(final @NonNull ReaderDevice readerDevice) {
        // not used in MOCK
    }

    @Override
    public void evaluatePermissionResults(final List<PermissionResult> results) {
        // not used in MOCK
    }

    public void syncData() {
        disposeDisposable();
        disposable = readerInteractor.syncAllResults()
                .subscribe(
                        ignored -> ifViewAttached(TurnReaderOnConnectView::navigateToHome),
                        error -> Timber.d("Error while running syncAllResults: %s", error)
                );
    }

    private void disposeDisposable() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }
}
