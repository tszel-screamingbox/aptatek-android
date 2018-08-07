package com.aptatek.aptatek.data.datasource;

import android.util.Pair;

import com.aptatek.aptatek.data.model.CubeDataModel;
import com.aptatek.aptatek.domain.interactor.cube.CubeDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO This DataSource implementation is currently used in PROD builds, but as soon as we integrate the real cube SDK, this class should be moved to MOCK flavor.
public class FakeCubeDataSourceImpl implements CubeDataSource {

    private final Map<Pair<Long,Long>, List<CubeDataModel>> cachedRandomData = new HashMap<>();
    private final FakeCubeDataGenerator dataGenerator;

    public FakeCubeDataSourceImpl(final FakeCubeDataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
    }

    @Override
    public List<CubeDataModel> getDataBetween(long startTime, long endTime) {
        final Pair<Long, Long> key = new Pair<>(startTime, endTime);
        if (cachedRandomData.containsKey(key)) {
            return cachedRandomData.get(key);
        } else {
            final List<CubeDataModel> cubeDataModels = dataGenerator.generateDataBetween(startTime, endTime);
            cachedRandomData.put(key, cubeDataModels);
            return cubeDataModels;
        }
    }

    @Override
    public CubeDataModel getOldestData() {
        return dataGenerator.generateOldestData();
    }
}
