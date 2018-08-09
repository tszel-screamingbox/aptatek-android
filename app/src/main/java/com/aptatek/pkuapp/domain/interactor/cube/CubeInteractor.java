package com.aptatek.pkuapp.domain.interactor.cube;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.model.CubeData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class CubeInteractor {

    private final CubeDataRepository dataRepository;

    @Inject
    CubeInteractor(CubeDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    // TODO remove this as soon as the chart on the Main screen uses pagination to get the data...
    @NonNull
    public Single<List<CubeData>> listAll() {
        return dataRepository.listAll();
    }

    @NonNull
    public Single<List<CubeData>> listBetween(final long start, final long end) {
        return dataRepository.listBetween(start, end);
    }

}