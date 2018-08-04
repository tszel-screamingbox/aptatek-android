package com.aptatek.aptatek.data.datasource;

import com.aptatek.aptatek.data.model.CubeDataModel;
import com.aptatek.aptatek.domain.interactor.cube.CubeDataSource;

import java.util.List;

// TODO This DataSource implementation is currently used in PROD builds, but as soon as we integrate the real cube SDK, this class should be moved to MOCK flavor.
public class FakeCubeDataSourceImpl implements CubeDataSource {

    private final FakeCubeDataGenerator dataGenerator;

    public FakeCubeDataSourceImpl(final FakeCubeDataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
    }

    @Override
    public List<CubeDataModel> getDataBetween(long startTime, long endTime) {
        return dataGenerator.generateDataBetween(startTime, endTime);
    }

    @Override
    public CubeDataModel getOldestData() {
        return dataGenerator.generateOldestData();
    }
}
