package com.aptatek.pkulab.domain.interactor.testresult;


import androidx.annotation.NonNull;

import com.aptatek.pkulab.domain.model.reader.TestResult;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class TestResultInteractor {

    private final TestResultRepository dataRepository;

    @Inject
    TestResultInteractor(final TestResultRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    // TODO remove this as soon as the chart on the Main screen uses pagination to get the data...
    @NonNull
    public Flowable<List<TestResult>> listAll() {
        return dataRepository.listAll();
    }

    @NonNull
    public Flowable<List<TestResult>> listBetween(final long start, final long end) {
        return dataRepository.listBetween(start, end);
    }

    public Single<TestResult> getById(@NonNull final String testId) {
        return dataRepository.getById(testId);
    }

    public Single<TestResult> getLatest() {
        return dataRepository.getLatest();
    }

    public Single<TestResult> getOldest() {
        return dataRepository.getOldest();
    }
}
