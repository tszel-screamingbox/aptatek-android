package com.aptatek.pkuapp.domain.interactor.incubation;

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

public class IncubationInteractor {

    private final IncubationDataSource dataSource;
    private final CountdownTimeFormatter timeFormatter;

    @Inject
    public IncubationInteractor(@NonNull final IncubationDataSource dataSource,
                                @NonNull final CountdownTimeFormatter timeFormatter) {
        this.dataSource = dataSource;
        this.timeFormatter = timeFormatter;
    }

    public Single<IncubationStatus> getIncubationStatus() {
        return Single.fromCallable(dataSource::getIncubationStatus);
    }

    public Flowable<Countdown> getIncubationCountdown() {
        return getIncubationStatus()
                .toFlowable()
                .map(incubationStatus -> incubationStatus == IncubationStatus.RUNNING)
                .take(1)
                .flatMap(value -> {
                    if (value) {
                        return Flowable.just(dataSource.getIncubationStart())
                                .flatMap(startTime ->
                                        com.aptatek.pkuapp.domain.interactor.countdown.Countdown.countdown(
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

    public Completable resetIncubation() {
        return Completable.fromAction(dataSource::resetIncubation);
    }

    public Completable skipIncubation() {
        return Completable.fromAction(dataSource::skipIncubation);
    }
}
