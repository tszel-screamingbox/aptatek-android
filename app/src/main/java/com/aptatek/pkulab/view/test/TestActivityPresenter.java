package com.aptatek.pkulab.view.test;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingStatus;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;
import com.aptatek.pkulab.domain.manager.analytic.events.appstart.AppStartFromWetting;
import com.aptatek.pkulab.domain.manager.analytic.events.appstart.OpenFromBTNotification;
import com.aptatek.pkulab.domain.manager.analytic.events.test.CollectBloodDone;
import com.aptatek.pkulab.domain.manager.analytic.events.test.ConnectItAllDone;
import com.aptatek.pkulab.domain.manager.analytic.events.test.MixSampleDone;
import com.aptatek.pkulab.domain.manager.analytic.events.test.PokeFingertipDone;
import com.aptatek.pkulab.domain.manager.analytic.events.test.SampleWettingDone;
import com.aptatek.pkulab.domain.manager.analytic.events.test.TestCancelledDueToCriticalBattery;
import com.aptatek.pkulab.util.Constants;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

class TestActivityPresenter extends MvpBasePresenter<TestActivityView> {

    private final WettingInteractor wettingInteractor;
    private final TestInteractor testInteractor;
    private final DeviceHelper deviceHelper;
    private final IAnalyticsManager analyticsManager;

    private long screenDisplayedAtMs = 0L;

    private Disposable disposable;
    private Disposable cancelTest;

    @Inject
    TestActivityPresenter(final WettingInteractor wettingInteractor,
                          final TestInteractor testInteractor,
                          final DeviceHelper deviceHelper,
                          final IAnalyticsManager analyticsManager) {
        this.wettingInteractor = wettingInteractor;
        this.testInteractor = testInteractor;
        this.deviceHelper = deviceHelper;
        this.analyticsManager = analyticsManager;
    }

    void checkBattery(final TestScreens screen) {
        if (deviceHelper.isBatteryLow() && !deviceHelper.isBatteryCharging() && !(screen == TestScreens.CONNECT_IT_ALL || screen == TestScreens.TESTING || screen == TestScreens.CANCEL)) {
            cancelTest = testInteractor.resetTest()
                    .andThen(Completable.fromAction(() -> analyticsManager.logEvent(new TestCancelledDueToCriticalBattery(deviceHelper.getPhoneBattery()))))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> ifViewAttached(TestActivityView::showBatteryAlert));
        }
    }

    void storeDestroyTimestamp() {
        disposable = testInteractor.storeAppKilledTimestamp(System.currentTimeMillis())
                .subscribe();
    }

    public void showProperScreen() {
        disposable = testInteractor.getLastScreen()
                .onErrorReturnItem(TestScreens.PREP_TEST_KIT)
                .flatMap(lastScreen -> {
                    if (lastScreen == TestScreens.WETTING) {
                        return wettingInteractor.getWettingStatus()
                                .flatMap(wettingStatus -> {
                                    if (wettingStatus == WettingStatus.FINISHED) {
                                        analyticsManager.logEvent(new SampleWettingDone(Constants.DEFAULT_WETTING_PERIOD));
                                        return Single.just(TestScreens.TURN_READER_ON);
                                    }

                                    return Single.just(lastScreen);
                                });
                    }

                    return Single.just(lastScreen);
                })
                .flatMap(screen -> testInteractor.setLastScreen(screen).andThen(Single.just(screen)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(testScreens ->
                        ifViewAttached(attachedView -> {
                            attachedView.showScreen(testScreens);
                            screenDisplayedAtMs = System.currentTimeMillis();
                        })
                );
    }

    public void onShowNextScreen(@NonNull final TestScreens current) {
        disposable =
                Completable.fromAction(() -> {
                            final long elapsedScreenTimeMs = Math.abs(System.currentTimeMillis() - screenDisplayedAtMs);
                            final AnalyticsEvent event;
                            switch (current) {
                                case POKE_FINGERTIP: {
                                    event = new PokeFingertipDone(elapsedScreenTimeMs);
                                    break;
                                }
                                case COLLECT_BLOOD: {
                                    event = new CollectBloodDone(elapsedScreenTimeMs);
                                    break;
                                }
                                case MIX_SAMPLE: {
                                    event = new MixSampleDone(elapsedScreenTimeMs);
                                    break;
                                }
                                case CONNECT_IT_ALL: {
                                    event = new ConnectItAllDone(elapsedScreenTimeMs);
                                    break;
                                }
                                default: {
                                    event = null;
                                    break;
                                }
                            }

                            if (event != null) {
                                try {
                                    analyticsManager.logEvent(event);
                                } catch (Throwable t) {
                                    Timber.d("--- swallowed analytics error: %s", t);
                                }
                            }

                        })
                        .andThen(Flowable.fromCallable(() -> TestScreens.values()[current.ordinal() + 1]))
                        .doOnError(e -> Timber.d("--- showNextScreen error: %s", e))
                        .onErrorReturnItem(current)
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
                        .subscribe(nextScreen -> ifViewAttached(attachedView -> {
                                    attachedView.showScreen(nextScreen);
                                    screenDisplayedAtMs = System.currentTimeMillis();
                                })
                                , Timber::e);
    }

    @Override
    public void detachView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        if (cancelTest != null && !cancelTest.isDisposed()) {
            cancelTest.dispose();
        }

        super.detachView();
    }

    public void onShowPreviousScreen(@NonNull final TestScreens currentScreen) {
        ifViewAttached(attachedView -> {
            if (currentScreen == TestScreens.WETTING || currentScreen == TestScreens.PREP_TEST_KIT || currentScreen == TestScreens.PREPARE_CASSETTE || currentScreen == TestScreens.ATTACH_CHAMBER || currentScreen == TestScreens.TURN_READER_ON || currentScreen == TestScreens.TESTING || currentScreen == TestScreens.CONNECT_IT_ALL || currentScreen == TestScreens.TEST_COMPLETE) {
                attachedView.showScreen(TestScreens.CANCEL);
            } else {
                attachedView.showPreviousScreen();
            }
            screenDisplayedAtMs = System.currentTimeMillis();
        });
    }

    public void logOpenFromNotification(final String reason) {
        analyticsManager.logEvent(new OpenFromBTNotification(reason));
        analyticsManager.logEvent(new AppStartFromWetting());
    }
}
