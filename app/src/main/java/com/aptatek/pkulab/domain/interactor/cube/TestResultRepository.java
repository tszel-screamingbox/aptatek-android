package com.aptatek.pkulab.domain.interactor.cube;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.data.AptatekDatabase;
import com.aptatek.pkulab.data.dao.TestResultDao;
import com.aptatek.pkulab.data.model.TestResultDataModel;
import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.base.Repository;
import com.aptatek.pkulab.domain.model.reader.TestResult;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class TestResultRepository extends Repository<TestResult, TestResultDataModel> {

    private final CubeDataSource cubeDataSource;
    private final TestResultDao testResultDao;

    @Inject
    TestResultRepository(final Provider<Map<Class<?>, Mapper<?, ?>>> mapperProvider,
                         final CubeDataSource cubeDataSource,
                         final AptatekDatabase aptatekDatabase) {
        super(mapperProvider);

        this.cubeDataSource = cubeDataSource;
        this.testResultDao = aptatekDatabase.getTestResultDao();
    }

    @Override
    protected Class<?> getDomainClass() {
        return TestResult.class;
    }

    // TODO remove as soon as pagination is implemented
    @NonNull
    public Single<List<TestResult>> listAll() {
        final Single<List<TestResultDataModel>> listAll = testResultDao.getNumberOfRecords()
                .flatMap(numberOfRecords -> numberOfRecords == 0 ? mockDataList() : testResultDao.listAll());

        return listAll.map(mapper::mapListToDomain)
                .subscribeOn(Schedulers.io());
    }

    private Single<List<TestResultDataModel>> mockDataList() {
        return Single.fromCallable(() -> cubeDataSource.getDataBetween(cubeDataSource.getOldestData().getTimestamp(), TimeHelper.getLatestTimeAtGivenDay(System.currentTimeMillis())));
    }

    @NonNull
    public Single<List<TestResult>> listBetween(final long start, final long end) {
        final Single<List<TestResultDataModel>> listBetween = testResultDao.getNumberOfRecords()
                .flatMap(numberOfRecords -> numberOfRecords == 0 ? mockDataBetween(start, end) : testResultDao.listBetween(start, end));

        return listBetween.map(mapper::mapListToDomain)
                .subscribeOn(Schedulers.io());
    }

    private Single<List<TestResultDataModel>> mockDataBetween(final long start, final long end) {
        return Single.fromCallable(() -> cubeDataSource.getDataBetween(start, end));
    }

    @NonNull
    public Single<TestResult> getLatest() {
        final Single<TestResultDataModel> latest = testResultDao.getNumberOfRecords()
                .flatMap(numberOfRecords -> numberOfRecords == 0 ? mockLatest() : testResultDao.getLatestResult());

        return latest.map(mapper::mapToDomain)
                .subscribeOn(Schedulers.io());
    }

    private Single<TestResultDataModel> mockLatest() {
        return Single.fromCallable(cubeDataSource::getLatestData);
    }

    @NonNull
    public Completable insertAll(@NonNull final List<TestResult> testResults) {
        return Single.just(testResults)
                .map(mapper::mapListToData)
                .flatMapCompletable(dataModels -> Completable.fromAction(() -> testResultDao.insertAll(dataModels)))
                .subscribeOn(Schedulers.io());
    }
}
