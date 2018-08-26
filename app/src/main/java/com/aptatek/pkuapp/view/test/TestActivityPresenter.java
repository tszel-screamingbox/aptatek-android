package com.aptatek.pkuapp.view.test;

import android.util.Pair;

import com.aptatek.pkuapp.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationStatus;
import com.aptatek.pkuapp.domain.interactor.samplewetting.SampleWettingInteractor;
import com.aptatek.pkuapp.domain.interactor.samplewetting.WettingStatus;
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
                    incubationInteractor.getIncubationStatus(),
                    sampleWettingInteractor.getWettingStatus(),
                    Pair::new)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    value ->
                        ifViewAttached(attachedView -> {
                            if (value.second == WettingStatus.FINISHED) {
                                attachedView.showScreen(TestScreens.SAMPLE_WETTING);
                                attachedView.navigateForward();
                            } else if (value.second == WettingStatus.RUNNING) {
                                attachedView.showScreen(TestScreens.SAMPLE_WETTING);
                            } else if (value.first == IncubationStatus.RUNNING) {
                                attachedView.showScreen(TestScreens.INCUBATION);
                            } else if (value.first == IncubationStatus.FINISHED) {
                                attachedView.showScreen(TestScreens.INSERT_CASSETTE);
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
