package com.aptatek.pkulab.domain.interactor.testresult;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.data.model.TestResultDataModel;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.base.Repository;
import com.aptatek.pkulab.domain.model.reader.TestResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.aptatek.pkulab.device.time.TimeHelper.getEarliestTimeAtGivenDay;
import static com.aptatek.pkulab.device.time.TimeHelper.getLatestTimeAtGivenDay;

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
    public Flowable<List<TestResult>> listAll() {
        return Flowable.fromCallable(() -> testResultDataSource.getOldestData() != null)
                .flatMap(hasOldestData -> {
                    if (hasOldestData) {
                        final long start = getEarliestTimeAtGivenDay(testResultDataSource.getOldestData().getTimestamp());
                        final long end = getLatestTimeAtGivenDay(testResultDataSource.getLatestData().getTimestamp());

                        return testResultDataSource.getDataBetween(start, end);
                    } else {
                        return Flowable.just(Collections.<TestResultDataModel>emptyList());
                    }
                })
                .map(mapper::mapListToDomain)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Flowable<List<TestResult>> listBetween(final long start, final long end) {
        return testResultDataSource.getDataBetween(start, end)
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
    public Single<TestResult> getById(final String id) {
        return Single.fromCallable(() -> testResultDataSource.getById(id))
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
