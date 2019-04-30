package com.aptatek.pkulab.domain.interactor.testresult;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.data.model.TestResultDataModel;

import java.util.List;

import io.reactivex.Flowable;

public interface TestResultDataSource {

    Flowable<List<TestResultDataModel>> getDataBetween(long startTime, long endTime);

    TestResultDataModel getOldestData();

    TestResultDataModel getLatestData();

    TestResultDataModel getLatestFromReader(@NonNull final String readerId);

    TestResultDataModel getById(final String id);

    void insertAll(@NonNull List<TestResultDataModel> dataModels);

    int getNumberOfRecords();
}
