package com.aptatek.pkulab.domain.interactor.wetting;


import androidx.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.countdown.CountdownTimeFormatter;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.test.SampleWettingStarted;
import com.aptatek.pkulab.domain.model.Countdown;
import com.aptatek.pkulab.util.Constants;

import org.reactivestreams.Publisher;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class WettingInteractor {

    private final WettingDataSource wettingDataSource;
    private final CountdownTimeFormatter timeFormatter;
    private final IAnalyticsManager analyticsManager;

    @Inject
    public WettingInteractor(@NonNull final WettingDataSource wettingDataSource,
                             @NonNull final CountdownTimeFormatter timeFormatter,
                             final IAnalyticsManager analyticsManager) {
        this.wettingDataSource = wettingDataSource;
        this.timeFormatter = timeFormatter;
        this.analyticsManager = analyticsManager;
    }

    public Single<WettingStatus> getWettingStatus() {
        return Single.fromCallable(wettingDataSource::getWettingStatus);
    }

    public Flowable<Countdown> getWettingCountdown() {
        return getWettingStatus()
                .toFlowable()
                .map(status -> status == WettingStatus.RUNNING)
                .take(1)
                .flatMap(value -> {
                    if (value) {
                        return Flowable.fromCallable(wettingDataSource::getWettingStart)
                                .flatMap(startTime ->
                                        com.aptatek.pkulab.domain.interactor.countdown.Countdown.countdown(
                                            Constants.COUNTDOWN_REFRESH_PERIOD,
                                            tick -> System.currentTimeMillis() - startTime > Constants.DEFAULT_WETTING_PERIOD,
                                            tick -> Math.max(0, Constants.DEFAULT_WETTING_PERIOD - (System.currentTimeMillis() - startTime))
                                        )
                                        .map(remaining ->
                                            Countdown.builder()
                                                .setRemainingFormattedText(timeFormatter.getFormattedRemaining(remaining))
                                                .setRemainingMillis(remaining)
                                                .build()
                                        )
                                );
                    } else {
                        return Flowable.error(new WettingNotRunningError());
                    }
                })
                .onErrorResumeNext((Function<Throwable, Publisher<? extends Countdown>>) throwable ->
                        Flowable.error(throwable instanceof WettingNotRunningError ? throwable : new WettingError(throwable.getCause())));
    }

    public Completable startWetting() {
        return Completable.fromAction(wettingDataSource::startWetting)
                .andThen(Completable.fromAction(() -> analyticsManager.logEvent(new SampleWettingStarted())));
    }

    public Completable resetWetting() {
        return Completable.fromAction(wettingDataSource::resetWetting);
    }
}
