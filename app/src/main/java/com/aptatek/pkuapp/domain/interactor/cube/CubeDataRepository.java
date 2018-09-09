package com.aptatek.pkuapp.domain.interactor.cube;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.data.model.CubeDataModel;
import com.aptatek.pkuapp.device.time.TimeHelper;
import com.aptatek.pkuapp.domain.base.Mapper;
import com.aptatek.pkuapp.domain.base.Repository;
import com.aptatek.pkuapp.domain.model.CubeData;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class CubeDataRepository extends Repository<CubeData, CubeDataModel> {

    private final CubeDataSource cubeDataSource;

    @Inject
    CubeDataRepository(final Provider<Map<Class<?>, Mapper<?, ?>>> mapperProvider,
                       final CubeDataSource cubeDataSource) {
        super(mapperProvider);

        this.cubeDataSource = cubeDataSource;
    }

    @Override
    protected Class<?> getDomainClass() {
        return CubeData.class;
    }

    // TODO remove as soon as pagination is implemented
     @NonNull
    public Single<List<CubeData>> listAll() {
        return Single.fromCallable(() -> cubeDataSource.getDataBetween(cubeDataSource.getOldestData().getTimestamp(), TimeHelper.getLatestTimeAtGivenDay(System.currentTimeMillis())))
                .map(mapper::mapListToDomain)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<List<CubeData>> listBetween(final long start, final long end) {
        return Single.fromCallable(() -> cubeDataSource.getDataBetween(start, end))
                .map(mapper::mapListToDomain)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<CubeData> getLatest() {
        return Single.fromCallable(() -> cubeDataSource.getLatestData())
                .map(mapper::mapToDomain)
                .subscribeOn(Schedulers.io());
    }
}
