package com.aptatek.pkulab.view.test.dispose;

import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

class DisposePresenter extends MvpBasePresenter<DisposeView> {

    private final TestInteractor testInteractor;
    private Disposable disposable;

    @Inject
    DisposePresenter(final TestInteractor testInteractor) {
        this.testInteractor = testInteractor;
    }

    void done() {
        disposable = testInteractor.setTestContinueStatus(false)
                .subscribe();
        ifViewAttached(DisposeView::doneFinished);
    }

    @Override
    public void detachView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.detachView();
    }
}
