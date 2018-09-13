package com.aptatek.pkuapp.domain.interactor.wetting;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.interactor.countdown.CountdownTimeFormatter;
import com.aptatek.pkuapp.domain.model.Countdown;
import com.aptatek.pkuapp.util.Constants;

import org.reactivestreams.Publisher;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class WettingInteractor {

    private final WettingDataSource wettingDataSource;
    private final CountdownTimeFormatter timeFormatter;

    @Inject
    public WettingInteractor(@NonNull final WettingDataSource wettingDataSource,
                             @NonNull final CountdownTimeFormatter timeFormatter) {
        this.wettingDataSource = wettingDataSource;
        this.timeFormatter = timeFormatter;
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
                                        com.aptatek.pkuapp.domain.interactor.countdown.Countdown.countdown(
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
        return Completable.fromAction(wettingDataSource::startWetting);
    }

    public Completable resetWetting() {
        return Completable.fromAction(wettingDataSource::resetWetting);
    }
}
