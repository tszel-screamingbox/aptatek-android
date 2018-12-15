package com.aptatek.pkulab.domain.interactor.cube;

import com.aptatek.pkulab.data.model.TestResultDataModel;

import java.util.List;

public interface CubeDataSource {

    List<TestResultDataModel> getDataBetween(long startTime, long endTime);

    TestResultDataModel getOldestData();

    TestResultDataModel getLatestData();

}
