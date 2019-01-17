package com.aptatek.pkulab.view.test;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingStatus;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class TestActivityPresenter extends MvpBasePresenter<TestActivityView> {

    private final WettingInteractor wettingInteractor;
    private final DeviceHelper deviceHelper;

    private Disposable disposable;

    @Inject
    TestActivityPresenter(final WettingInteractor wettingInteractor,
                          final DeviceHelper deviceHelper) {
        this.wettingInteractor = wettingInteractor;
        this.deviceHelper = deviceHelper;
    }

    void checkBattery() {
        if (deviceHelper.isBatteryLow()) {
            ifViewAttached(TestActivityView::showBatteryAlert);
        }
    }

    public void showProperScreen(final boolean otherScreenDisplayed) {
        disposable = wettingInteractor.getWettingStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        wettingStatus ->
                                ifViewAttached(attachedView -> {
                                    if (wettingStatus == WettingStatus.FINISHED) {
                                        attachedView.showScreen(TestScreens.TURN_READER_ON);
                                    } else if (wettingStatus == WettingStatus.RUNNING) {
                                        attachedView.showScreen(TestScreens.WETTING);
                                    } else if (!otherScreenDisplayed) {
                                        attachedView.showScreen(TestScreens.BREAK_FOIL);
                                    }
                                })
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
            if (currentScreen == TestScreens.WETTING || currentScreen == TestScreens.TURN_READER_ON || currentScreen == TestScreens.TESTING) {
                attachedView.showScreen(TestScreens.CANCEL);
            } else {
                attachedView.showPreviousScreen();
            }
        });
    }
}
