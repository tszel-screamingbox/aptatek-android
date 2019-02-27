package com.aptatek.pkulab.domain.interactor.testresult;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.data.model.TestResultDataModel;

import java.util.List;

public interface TestResultDataSource {

    List<TestResultDataModel> getDataBetween(long startTime, long endTime);

    TestResultDataModel getOldestData();

    TestResultDataModel getLatestData();

    void insertAll(@NonNull List<TestResultDataModel> dataModels);

}
