package com.aptatek.aptatek.domain.interactor.cube;

import com.aptatek.aptatek.data.model.CubeDataModel;

import java.util.List;

public interface CubeDataSource {

    List<CubeDataModel> getDataBetween(long startTime, long endTime);

    CubeDataModel getOldestData();

}
