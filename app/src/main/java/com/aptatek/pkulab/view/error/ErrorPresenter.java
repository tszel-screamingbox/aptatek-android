package com.aptatek.pkulab.view.error;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

public class ErrorPresenter extends MvpBasePresenter<ErrorView> {

    private final TestInteractor testInteractor;
    private final PreferenceManager preferenceManager;
    private Disposable disposable;

    @Inject
    ErrorPresenter(final TestInteractor testInteractor,
                   final PreferenceManager preferenceManager) {
        this.testInteractor = testInteractor;
        this.preferenceManager = preferenceManager;
    }

    void done() {
        disposable = testInteractor.setTestContinueStatus(false)
                .andThen(Completable.fromAction(() -> {
                    preferenceManager.clearPreference(PreferenceManager.PREF_TEST_START);
                }))
                .subscribe(() -> ifViewAttached(ErrorView::doneFinished));
    }

    @Override
    public void detachView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.detachView();
    }
}
