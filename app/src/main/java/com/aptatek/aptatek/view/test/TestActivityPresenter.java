package com.aptatek.aptatek.view.test;

import android.util.Pair;

import com.aptatek.aptatek.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.aptatek.domain.interactor.samplewetting.SampleWettingInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

class TestActivityPresenter extends MvpBasePresenter<TestActivityView> {

    private final IncubationInteractor incubationInteractor;

    private final SampleWettingInteractor sampleWettingInteractor;

    private Disposable disposable;

    @Inject
    TestActivityPresenter(final IncubationInteractor incubationInteractor,
                          final SampleWettingInteractor sampleWettingInteractor) {
        this.incubationInteractor = incubationInteractor;
        this.sampleWettingInteractor = sampleWettingInteractor;
    }

    public void showProperScreen(final boolean otherScreenDisplayed) {
        disposable =
                Single.zip(
                    incubationInteractor.hasRunningIncubation(),
                    sampleWettingInteractor.hasRunningWetting(),
                    Pair::new)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    value ->
                        ifViewAttached(attachedView -> {
                            if (value.second) {
                                attachedView.showScreen(TestScreens.SAMPLE_WETTING);
                            } else if (value.first) {
                                attachedView.showScreen(TestScreens.INCUBATION);
                            } else if (!otherScreenDisplayed) {
                                attachedView.showScreen(TestScreens.TAKE_SAMPLE);
                            }
                        })
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
