package com.aptatek.aptatek.domain.interactor.incubation;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.domain.interactor.countdown.CountdownTimeFormatter;
import com.aptatek.aptatek.domain.model.Countdown;
import com.aptatek.aptatek.util.Constants;

import org.reactivestreams.Publisher;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class IncubationInteractor {

    private final IncubationDataSource dataSource;
    private final CountdownTimeFormatter timeFormatter;

    @Inject
    public IncubationInteractor(@NonNull final IncubationDataSource dataSource,
                                @NonNull final CountdownTimeFormatter timeFormatter) {
        this.dataSource = dataSource;
        this.timeFormatter = timeFormatter;
    }

    public Single<Boolean> hasRunningIncubation() {
        return Single.fromCallable(dataSource::hasRunningIncubation);
    }

    public Flowable<Countdown> getIncubationCountdown() {
        return hasRunningIncubation()
                .toFlowable()
                .take(1)
                .flatMap(value -> {
                    if (value) {
                        return Flowable.just(dataSource.getIncubationStart())
                                .flatMap(startTime ->
                                        com.aptatek.aptatek.domain.interactor.countdown.Countdown.countdown(
                                            Constants.COUNTDOWN_REFRESH_PERIOD,
                                            tick -> System.currentTimeMillis() - startTime > Constants.DEFAULT_INCUBATION_PERIOD,
                                            tick -> Math.max(0, Constants.DEFAULT_INCUBATION_PERIOD - (System.currentTimeMillis() - startTime))
                                        )
                                        .map(remaining -> Countdown.builder()
                                                        .setRemainingFormattedText(timeFormatter.getFormattedRemaining(remaining))
                                                        .setRemainingMillis(remaining)
                                                        .build())
                                );
                    } else {
                        return Flowable.error(new IncubationNotRunningError());
                    }
                })
                .onErrorResumeNext((Function<Throwable, Publisher<? extends Countdown>>) throwable ->
                        Flowable.error(throwable instanceof IncubationNotRunningError ? throwable : new IncubationError(throwable.getCause())));
    }

    public Completable startIncubation() {
        return Completable.fromAction(dataSource::startIncubation);
    }

    public Completable stopIncubation() {
        return Completable.fromAction(dataSource::stopIncubation);
    }
}
