package com.aptatek.pkuapp.view.test;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.pkuapp.domain.interactor.samplewetting.SampleWettingInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

//    public void showProperScreen(final boolean otherScreenDisplayed) {
//        disposable =
//                Single.zip(
//                    incubationInteractor.getIncubationStatus(),
//                    sampleWettingInteractor.getWettingStatus(),
//                    Pair::new)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    value ->
//                        ifViewAttached(attachedView -> {
//                            attachedView.showScreen(TestScreens.BREAK_FOIL);
//                            if (value.second == WettingStatus.FINISHED) {
//                                attachedView.showScreen(TestScreens.SAMPLE_WETTING);
//                                attachedView.showNextScreen();
//                            } else if (value.second == WettingStatus.RUNNING) {
//                                attachedView.showScreen(TestScreens.SAMPLE_WETTING);
//                            } else if (value.first == IncubationStatus.RUNNING) {
//                                attachedView.showScreen(TestScreens.INCUBATION);
//                            } else if (value.first == IncubationStatus.FINISHED) {
//                                attachedView.showScreen(TestScreens.INSERT_CASSETTE);
//                            } else if (!otherScreenDisplayed) {
//                                attachedView.showScreen(TestScreens.TAKE_SAMPLE);
//                            }
//                        })
//                    );
//    }

    public void onShowNextScreen(@NonNull final TestScreens current) {
        disposable = Flowable.fromCallable(() -> TestScreens.values()[current.ordinal() + 1])
                .flatMap(nextScreen -> {
                    if (nextScreen == TestScreens.WETTING) {
                        return sampleWettingInteractor.startWetting()
                                .andThen(Flowable.just(nextScreen));
                    }

                    return Flowable.just(nextScreen);
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(nextScreen -> ifViewAttached(attachedView -> attachedView.showScreen(nextScreen)));
    }

    @Override
    public void detachView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.detachView();
    }
}
