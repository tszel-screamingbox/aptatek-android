package com.aptatek.aptatek.domain.interactor.wetting;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.domain.interactor.countdown.CountdownTimeFormatter;
import com.aptatek.aptatek.domain.model.Countdown;
import com.aptatek.aptatek.domain.model.WettingStatus;
import com.aptatek.aptatek.util.Constants;

import org.reactivestreams.Publisher;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.processors.BehaviorProcessor;

public class SampleWettingInteractor {

    private final WettingDataSource wettingDataSource;
    private final CountdownTimeFormatter timeFormatter;
    private final BehaviorProcessor<WettingStatus> wettingStatus;

    @Inject
    public SampleWettingInteractor(@NonNull final WettingDataSource wettingDataSource,
                                   @NonNull final CountdownTimeFormatter timeFormatter) {
        this.wettingDataSource = wettingDataSource;
        this.timeFormatter = timeFormatter;
        wettingStatus = BehaviorProcessor.create();
    }

    public Single<Boolean> hasRunningWetting() {
        return Single.fromCallable(wettingDataSource::hasRunningWetting);
    }

    public Flowable<Countdown> getWettingCountdown() {
        return hasRunningWetting()
                .toFlowable()
                .take(1)
                .flatMap(value -> {
                    if (value) {
                        return Flowable.just(wettingDataSource.getWettingStart())
                                .flatMap(startTime ->
                                        com.aptatek.aptatek.domain.interactor.countdown.Countdown.countdown(
                                            Constants.COUNTDOWN_REFRESH_PERIOD,
                                            tick -> System.currentTimeMillis() - startTime > Constants.DEFAULT_WETTING_PERIOD,
                                            tick -> Math.max(0, Constants.DEFAULT_WETTING_PERIOD - (System.currentTimeMillis() - startTime))
                                        )
                                        .map(remaining -> {
                                            final int thirdPeriod = (int) Constants.DEFAULT_WETTING_PERIOD / 3;
                                            final int currentProgress = (int) (remaining % thirdPeriod);

                                            final WettingStatus currentStatus;

                                            switch (currentProgress) {
                                                case 1:
                                                    currentStatus = WettingStatus.SECOND_THIRD;
                                                    break;
                                                case 2:
                                                    currentStatus = WettingStatus.THIRD_THIRD;
                                                    break;
                                                case 0:
                                                default:
                                                    currentStatus = WettingStatus.FIRST_THIRD;
                                                    break;
                                            }

                                            wettingStatus.onNext(currentStatus);

                                            return Countdown.builder()
                                                .setRemainingFormattedText(timeFormatter.getFormattedRemaining(remaining))
                                                .setRemainingMillis(remaining)
                                                .build();
                                        })
                                );
                    } else {
                        return Flowable.error(new WettingNotRunningError());
                    }
                })
                .onErrorResumeNext((Function<Throwable, Publisher<? extends Countdown>>) throwable ->
                        Flowable.error(throwable instanceof WettingNotRunningError ? throwable : new WettingError(throwable.getCause())));
    }

    public Flowable<WettingStatus> getWettingStatus() {
        return wettingStatus;
    }

    public Completable startWetting() {
        return Completable.fromAction(wettingDataSource::startWetting);
    }

    public Completable stopWetting() {
        return Completable.fromAction(wettingDataSource::stopWetting);
    }
}
