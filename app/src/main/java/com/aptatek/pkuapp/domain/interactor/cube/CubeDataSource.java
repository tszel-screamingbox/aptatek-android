package com.aptatek.pkuapp.domain.interactor.cube;

import com.aptatek.pkuapp.data.model.CubeDataModel;

import java.util.List;

public interface CubeDataSource {

    List<CubeDataModel> getDataBetween(long startTime, long endTime);

    CubeDataModel getOldestData();

    CubeDataModel getLatestData();

}
