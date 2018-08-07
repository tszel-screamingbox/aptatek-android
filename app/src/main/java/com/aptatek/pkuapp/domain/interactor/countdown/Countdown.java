package com.aptatek.pkuapp.domain.interactor.countdown;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public final class Countdown {

    private Countdown() {}

    public static Flowable<Long> countdown(final long intervalMs,
                                      @NonNull final Predicate<Long> until,
                                      @NonNull final Function<Long, Long> calculateRemainingMs) {
        return Flowable.interval(intervalMs, TimeUnit.MILLISECONDS)
                .takeUntil(until)
                .map(calculateRemainingMs);
    }

}
