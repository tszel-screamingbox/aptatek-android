package com.aptatek.pkulab.domain.interactor.cube;

import com.aptatek.pkulab.data.model.CubeDataModel;

import java.util.List;

public interface CubeDataSource {

    List<CubeDataModel> getDataBetween(long startTime, long endTime);

    CubeDataModel getOldestData();

    CubeDataModel getLatestData();

}
