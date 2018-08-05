package com.aptatek.aptatek.data.datasource;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.data.model.CubeDataModel;
import com.aptatek.aptatek.device.time.TimeHelper;
import com.aptatek.aptatek.util.Constants;

import org.fluttercode.datafactory.impl.DataFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@SuppressWarnings("magicNumber")
public class FakeCubeDataGenerator {

    private static final long DAY_IN_MILLIS = 1000L * 60 * 60 * 24;
    private static final int MAX_MEASUREMENTS_PER_DAY = 5;
    private static final int FIRST_DATA_BEFORE_TODAY_IN_DAYS = 7 * 3 - 2;

    private final DataFactory dataFactory;

    @Inject
    FakeCubeDataGenerator() {
        dataFactory = new DataFactory();
    }

    @NonNull
    public List<CubeDataModel> generateDataBetween(final long begin, final long end) {
        final List<CubeDataModel> cubeDataModels = new ArrayList<>();
        final int daysBetween = TimeHelper.getDaysBetween(begin, end);

        for (int day = 0; day <= daysBetween; day++) {
            final int numOfMeasurementsThatDay = dataFactory.getNumberUpTo(MAX_MEASUREMENTS_PER_DAY);
            for (int measurement = 0; measurement < numOfMeasurementsThatDay; measurement++) {
                final CubeDataModel cubeDataModel = generateDataForGivenDay(TimeHelper.addDays(day, begin));
                cubeDataModels.add(cubeDataModel);
            }
        }

        return cubeDataModels;
    }

    @NonNull
    public CubeDataModel generateDataForGivenDay(final long timestamp) {
        final CubeDataModel cubeDataModel = new CubeDataModel();
        cubeDataModel.setCubeId(dataFactory.getRandomChars(10));
        cubeDataModel.setId(dataFactory.getNumberUpTo(Integer.MAX_VALUE));
        cubeDataModel.setValueInMMol(dataFactory.getNumberUpTo((int) Constants.DEFAULT_PKU_HIGHEST_VALUE));
        final long earliestTimeAtGivenDay = TimeHelper.getEarliestTimeAtGivenDay(timestamp);
        final long randomOffset = dataFactory.getNumberUpTo((int) DAY_IN_MILLIS);
        cubeDataModel.setTimestamp(earliestTimeAtGivenDay + randomOffset);

        return cubeDataModel;
    }

    @NonNull
    public CubeDataModel generateOldestData() {
        return generateDataForGivenDay(TimeHelper.addDays(FIRST_DATA_BEFORE_TODAY_IN_DAYS * -1, System.currentTimeMillis()));
    }

}
