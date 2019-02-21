package com.aptatek.pkulab.domain.interactor.testresult;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.data.model.TestResultDataModel;
import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.base.Repository;
import com.aptatek.pkulab.domain.model.reader.TestResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class TestResultRepository extends Repository<TestResult, TestResultDataModel> {

    private final TestResultDataSource testResultDataSource;

    @Inject
    TestResultRepository(final Provider<Map<Class<?>, Mapper<?, ?>>> mapperProvider,
                         final TestResultDataSource testResultDataSource) {
        super(mapperProvider);

        this.testResultDataSource = testResultDataSource;
    }

    @Override
    protected Class<?> getDomainClass() {
        return TestResult.class;
    }

    // TODO remove as soon as pagination is implemented
    @NonNull
    public Single<List<TestResult>> listAll() {
        return Single.fromCallable(() -> {
            if (testResultDataSource.getOldestData() != null) {
                return testResultDataSource.getDataBetween(testResultDataSource.getOldestData().getTimestamp(), TimeHelper.getLatestTimeAtGivenDay(System.currentTimeMillis()));
            }

            return Collections.<TestResultDataModel>emptyList();
        })
        .map(mapper::mapListToDomain)
        .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<List<TestResult>> listBetween(final long start, final long end) {
        return Single.fromCallable(() -> testResultDataSource.getDataBetween(start, end))
                .map(mapper::mapListToDomain)
                .subscribeOn(Schedulers.io());
    }
    
    @NonNull
    public Single<TestResult> getLatest() {
        return Single.fromCallable(testResultDataSource::getLatestData)
                .map(mapper::mapToDomain)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<TestResult> getOldest() {
        return Single.fromCallable(testResultDataSource::getOldestData)
                .map(mapper::mapToDomain)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Completable insertAll(@NonNull final List<TestResult> testResults) {
        return Single.just(testResults)
                .map(mapper::mapListToData)
                .flatMapCompletable(dataModels -> Completable.fromAction(() -> testResultDataSource.insertAll(dataModels)))
                .subscribeOn(Schedulers.io());
    }
}
