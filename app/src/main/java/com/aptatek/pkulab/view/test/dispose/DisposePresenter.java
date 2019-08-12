package com.aptatek.pkulab.view.test.dispose;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.test.TestingDone;
import com.aptatek.pkulab.domain.manager.analytic.events.test.WasteDisposableDone;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

class DisposePresenter extends MvpBasePresenter<DisposeView> {

    private final TestInteractor testInteractor;
    private final IAnalyticsManager analyticsManager;
    private final PreferenceManager preferenceManager;
    private Disposable disposable;
    private long screenDisplayedAtMs = 0L;

    @Inject
    DisposePresenter(final TestInteractor testInteractor,
                     final IAnalyticsManager analyticsManager,
                     final PreferenceManager preferenceManager) {
        this.testInteractor = testInteractor;
        this.analyticsManager = analyticsManager;
        this.preferenceManager = preferenceManager;

        screenDisplayedAtMs = System.currentTimeMillis();
    }

    void done() {
        disposable = testInteractor.setTestContinueStatus(false)
                .andThen(Completable.fromAction(() -> {
                    analyticsManager.logEvent(new WasteDisposableDone(Math.abs(System.currentTimeMillis() - screenDisplayedAtMs)));
                    analyticsManager.logEvent(new TestingDone(Math.abs(System.currentTimeMillis() - preferenceManager.getTestFlowStart())));
                    preferenceManager.clearPreference(PreferenceManager.PREF_TEST_START);
                }))
                .subscribe(() -> ifViewAttached(DisposeView::doneFinished));
    }

    @Override
    public void detachView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.detachView();
    }
}
