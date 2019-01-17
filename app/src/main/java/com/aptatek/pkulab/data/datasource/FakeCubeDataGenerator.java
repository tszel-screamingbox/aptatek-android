package com.aptatek.pkulab.data.datasource;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.data.model.TestResultDataModel;
import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.util.Constants;

import org.fluttercode.datafactory.impl.DataFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ix.Ix;

@SuppressWarnings("magicNumber")
public class FakeCubeDataGenerator {

    private static final long DAY_IN_MILLIS = 1000L * 60 * 60 * 24;
    private static final long MIN_TIME_BETWEEN_MEASURES = 1000L * 60 * 40;
    private static final int MAX_MEASUREMENTS_PER_DAY = 5;
    private static final int FIRST_DATA_BEFORE_TODAY_IN_DAYS = 7 * 3 - 2;
    private static final int SICK_CHANCE = 5;
    private static final int FASTING_CHANCE = 10;

    private final DataFactory dataFactory;

    @Inject
    FakeCubeDataGenerator() {
        dataFactory = new DataFactory();
    }

    @NonNull
    public List<TestResultDataModel> generateDataBetween(final long begin, final long end) {
        final List<TestResultDataModel> testResultDataModels = new ArrayList<>();
        final int daysBetween = TimeHelper.getDaysBetween(begin, end);

        for (int day = 0; day <= daysBetween; day++) {
            final int numOfMeasurementsThatDay = dataFactory.getNumberUpTo(MAX_MEASUREMENTS_PER_DAY);
            for (int measurement = 0; measurement < numOfMeasurementsThatDay; measurement++) {
                final TestResultDataModel testResultDataModel = generateDataForGivenDay(TimeHelper.addDays(day, begin));

                ensureCubeDataTimeBetweenRandomData(testResultDataModels, testResultDataModel);

                testResultDataModels.add(testResultDataModel);
            }
        }

        return testResultDataModels;
    }

    private void ensureCubeDataTimeBetweenRandomData(final List<TestResultDataModel> randomModels, final TestResultDataModel testResultDataModel) {
        final boolean hasCollision = Ix.from(randomModels)
            .filter(model -> Math.abs(testResultDataModel.getTimestamp() - model.getTimestamp()) < MIN_TIME_BETWEEN_MEASURES)
            .count()
            .single() > 0;
        if (hasCollision) {
            testResultDataModel.setTimestamp(generateRandomTimeAtGivenDay(testResultDataModel.getTimestamp()));
        }
    }

    @NonNull
    public TestResultDataModel generateDataForGivenDay(final long timestamp) {
        final TestResultDataModel testResultDataModel = new TestResultDataModel();
        testResultDataModel.setReaderId(dataFactory.getRandomChars(10));
        testResultDataModel.setId(dataFactory.getRandomChars(10));
        testResultDataModel.setPkuLevel(PkuLevel.create(dataFactory.getNumberUpTo((int) Constants.DEFAULT_PKU_HIGHEST_VALUE), PkuLevelUnits.MICRO_MOL));
        testResultDataModel.setTimestamp(generateRandomTimeAtGivenDay(timestamp));
        testResultDataModel.setSick(dataFactory.chance(SICK_CHANCE));
        testResultDataModel.setFasting(dataFactory.chance(FASTING_CHANCE));

        return testResultDataModel;
    }

    private long generateRandomTimeAtGivenDay(final long timestamp) {
        final long earliestTimeAtGivenDay = TimeHelper.getEarliestTimeAtGivenDay(timestamp);
        final long randomOffset = dataFactory.getNumberUpTo((int) DAY_IN_MILLIS);
        return earliestTimeAtGivenDay + randomOffset;
    }

    @NonNull
    public TestResultDataModel generateOldestData() {
        final long earliestTimeAtGivenDay = TimeHelper.getEarliestTimeAtGivenDay(System.currentTimeMillis());
        final long oldestTimestamp = TimeHelper.addDays(FIRST_DATA_BEFORE_TODAY_IN_DAYS * -1, earliestTimeAtGivenDay);
        final TestResultDataModel testResultDataModel = generateDataForGivenDay(oldestTimestamp);
        testResultDataModel.setTimestamp(oldestTimestamp);

        return testResultDataModel;
    }

}
