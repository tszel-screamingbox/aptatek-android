package com.aptatek.aptatek.view.test;

import com.aptatek.aptatek.domain.interactor.incubation.IncubationInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

class TestActivityPresenter extends MvpBasePresenter<TestActivityView> {

    private final IncubationInteractor incubationInteractor;

    private Disposable disposable;

    @Inject
    TestActivityPresenter(final IncubationInteractor incubationInteractor) {
        this.incubationInteractor = incubationInteractor;
    }

    public void showProperScreen() {
        disposable = incubationInteractor.hasRunningIncubation()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    value ->
                        ifViewAttached(attachedView ->
                                attachedView.showScreen(value ? TestScreens.INCUBATION : TestScreens.TAKE_SAMPLE))
        );
    }

    @Override
    public void detachView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.detachView();
    }
}
