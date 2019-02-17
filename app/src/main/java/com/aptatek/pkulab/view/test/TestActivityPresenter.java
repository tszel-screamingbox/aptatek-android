package com.aptatek.pkulab.view.test;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingStatus;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class TestActivityPresenter extends MvpBasePresenter<TestActivityView> {

    private final WettingInteractor wettingInteractor;
    private final TestInteractor testInteractor;
    private final DeviceHelper deviceHelper;

    private Disposable disposable;

    @Inject
    TestActivityPresenter(final WettingInteractor wettingInteractor,
                          final TestInteractor testInteractor,
                          final DeviceHelper deviceHelper) {
        this.wettingInteractor = wettingInteractor;
        this.testInteractor = testInteractor;
        this.deviceHelper = deviceHelper;
    }

    void checkBattery() {
        if (deviceHelper.isBatteryLow()) {
            ifViewAttached(TestActivityView::showBatteryAlert);
        }
    }

    public void showProperScreen() {
        disposable = testInteractor.getLastScreen()
                .flatMap(lastScreen -> {
                    if (lastScreen == TestScreens.WETTING) {
                        return wettingInteractor.getWettingStatus()
                                .flatMap(wettingStatus -> {
                                    if (wettingStatus == WettingStatus.FINISHED) {
                                        // TODO: should check whether the reader is connected
                                        return Single.just(TestScreens.CONNECT_IT_ALL);
                                    }

                                    return Single.just(lastScreen);
                                });
                    }

                    return Single.just(lastScreen);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(testScreens ->
                        ifViewAttached(attachedView -> attachedView.showScreen(testScreens))
                );
    }

    public void onShowNextScreen(@NonNull final TestScreens current) {
        disposable = Flowable.fromCallable(() -> TestScreens.values()[current.ordinal() + 1])
                .flatMap(nextScreen -> {
                    if (nextScreen == TestScreens.WETTING) {
                        return wettingInteractor.startWetting()
                                .andThen(Flowable.just(nextScreen));
                    }
                    return Flowable.just(nextScreen);
                })
                .flatMap(nextScreen -> testInteractor.setLastScreen(nextScreen)
                        .andThen(Flowable.just(nextScreen)))
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

    public void onShowPreviousScreen(@NonNull final TestScreens currentScreen) {
        ifViewAttached(attachedView -> {
            if (currentScreen == TestScreens.TURN_READER_ON || currentScreen == TestScreens.WETTING || currentScreen == TestScreens.TESTING || currentScreen == TestScreens.BREAK_FOIL || currentScreen == TestScreens.CONNECT_IT_ALL) {
                attachedView.showScreen(TestScreens.CANCEL);
            } else {
                attachedView.showPreviousScreen();
            }
        });
    }
}
