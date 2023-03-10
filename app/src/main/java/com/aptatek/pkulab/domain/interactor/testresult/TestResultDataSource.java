package com.aptatek.pkulab.domain.interactor.testresult;


import androidx.annotation.NonNull;

import com.aptatek.pkulab.data.model.TestResultDataModel;

import java.util.List;

import io.reactivex.Flowable;

public interface TestResultDataSource {

    Flowable<List<TestResultDataModel>> getDataBetween(long startTime, long endTime);

    TestResultDataModel getOldestData();

    TestResultDataModel getLatestData();

    TestResultDataModel getLatestFromReader(@NonNull final String readerMac);

    TestResultDataModel getById(final String id);

    void insertAll(@NonNull List<TestResultDataModel> dataModels);

    void insert(TestResultDataModel dataModel);

    int getNumberOfRecords();
}
