package com.aptatek.pkulab.view.connect.onboarding.turnon;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.test.ErrorInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.onboarding.OnboardingReaderConnected;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenter;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenterImpl;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class TurnReaderOnConnectPresenter extends MvpBasePresenter<TurnReaderOnConnectView> implements TurnReaderOnPresenter<TurnReaderOnConnectView> {

    private final TurnReaderOnPresenterImpl wrapped;
    private final ReaderInteractor readerInteractor;
    private final IAnalyticsManager analyticsManager;
    private Disposable disposable;

    private Disposable syncProgressDisposable;

    @Inject
    public TurnReaderOnConnectPresenter(final BluetoothInteractor bluetoothInteractor,
                                        final ReaderInteractor readerInteractor,
                                        final TestInteractor testInteractor,
                                        final IAnalyticsManager analyticsManager,
                                        final ErrorInteractor errorInteractor) {
        this.analyticsManager = analyticsManager;
        wrapped = new TurnReaderOnPresenterImpl(bluetoothInteractor, readerInteractor, testInteractor, analyticsManager, errorInteractor);
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
        analyticsManager.logEvent(new OnboardingReaderConnected());

        disposeDisposable();
        disposable = readerInteractor.syncResultsAfterLast()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> ifViewAttached(TurnReaderOnConnectView::navigateToHome),
                        error -> {
                            Timber.d("Error while running syncAllResults: %s", error);
                            ifViewAttached(TurnReaderOnConnectView::showFailedToSync);
                        }
                );

        syncProgressDisposable = readerInteractor.syncProgressFlowable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        syncProgress -> ifViewAttached(av -> av.showSyncResultsScreen(syncProgress)),
                        error -> Timber.d("Error while reading sync progress: %s", error)
                );
    }

    private void disposeDisposable() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }

        if (syncProgressDisposable != null && !syncProgressDisposable.isDisposed()) {
            syncProgressDisposable.dispose();
            syncProgressDisposable = null;
        }
    }

    @Override
    public void logScreenDisplayed() {
        wrapped.logScreenDisplayed();
    }

    @Override
    public void disposeTest() {
        wrapped.disposeTest();
    }
}
