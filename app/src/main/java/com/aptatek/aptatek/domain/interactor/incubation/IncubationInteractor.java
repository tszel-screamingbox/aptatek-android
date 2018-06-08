package com.aptatek.aptatek.domain.interactor.incubation;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class IncubationInteractor {

    private final IncubationDataSource dataSource;

    @Inject
    public IncubationInteractor(@NonNull final IncubationDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Single<Boolean> hasRunningIncubation() {
        return Single.just(dataSource.hasRunningIncubation());
    }

    public Single<Long> getIncubationStart() {
        return Single.just(dataSource.getIncubationStart());
    }

    public Completable startIncubation() {
        return Completable.fromAction(dataSource::startIncubation);
    }

    public Completable stopIncubation() {
        return Completable.fromAction(dataSource::stopIncubation);
    }
}
