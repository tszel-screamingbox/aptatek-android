package com.aptatek.aptatek.domain.interactor.incubation;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.domain.model.IncubationCountdown;
import com.aptatek.aptatek.util.Constants;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class IncubationInteractor {

    private final IncubationDataSource dataSource;
    private final IncubationTimeFormatter timeFormatter;

    @Inject
    public IncubationInteractor(@NonNull final IncubationDataSource dataSource,
                                @NonNull final IncubationTimeFormatter timeFormatter) {
        this.dataSource = dataSource;
        this.timeFormatter = timeFormatter;
    }

    public Single<Boolean> hasRunningIncubation() {
        return Single.just(dataSource.hasRunningIncubation());
    }

    public Flowable<IncubationCountdown> getIncubationCountdown() {
        return hasRunningIncubation()
                .toFlowable()
                .take(1)
                .flatMap(value -> {
                    if (value) {
                        return Flowable.just(dataSource.getIncubationStart())
                                .flatMap(startTime ->
                                        Flowable.interval(Constants.COUNTDOWN_REFRESH_PERIOD, TimeUnit.MILLISECONDS)
                                                .map(tick -> System.currentTimeMillis() - startTime)
                                                .takeUntil(elapsed -> elapsed > Constants.DEFAULT_INCUBATION_PERIOD)
                                                .map(elapsed -> Math.max(0, Constants.DEFAULT_INCUBATION_PERIOD - elapsed))
                                                .map(remaining -> IncubationCountdown.builder()
                                                        .setRemainingFormattedText(timeFormatter.getFormattedRemaining(remaining))
                                                        .setRemainingMillis(remaining)
                                                        .build())
                                );
                    } else {
                        return Flowable.error(new IncubationNotRunningError());
                    }
                })
                .onErrorResumeNext((Function<Throwable, Publisher<? extends IncubationCountdown>>) throwable ->
                        Flowable.error(new IncubationError(throwable.getCause())));
    }

    public Completable startIncubation() {
        return Completable.fromAction(dataSource::startIncubation);
    }

    public Completable stopIncubation() {
        return Completable.fromAction(dataSource::stopIncubation);
    }
}
