package com.aptatek.pkulab.data.generator;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.reader.CartridgeInfo;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.util.Constants;

import org.fluttercode.datafactory.impl.DataFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import ix.Ix;

@SuppressWarnings("magicNumber")
public class FakeReaderDataGenerator {

    private static final long DAY_IN_MILLIS = 1000L * 60 * 60 * 24;
    private static final long MIN_TIME_BETWEEN_MEASURES = 1000L * 60 * 40;
    private static final int MAX_MEASUREMENTS_PER_DAY = 5;
    private static final int SICK_CHANCE = 5;
    private static final int FASTING_CHANCE = 10;

    private final DataFactory dataFactory;

    private final List<TestResult> testResults = new ArrayList<>();

    @Inject
    FakeReaderDataGenerator(final DataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    public int generateBatteryLevel() {
        return dataFactory.getNumberBetween(15, 100);
    }

    public CartridgeInfo generateCartridgeInfo() {
        final long now = System.currentTimeMillis();
        return CartridgeInfo.builder()
                .setType(dataFactory.getRandomChars(10))
                .setLot(dataFactory.getRandomChars(10))
                .setCalibration(dataFactory.getRandomChars(10))
                .setDate(TimeHelper.addMonths(-2, now))
                .setExpiry(TimeHelper.addMonths(12, now))
                .build();
    }

    public int getNumberOfResults() {
        if (testResults.isEmpty()) {
            generateTestResults();
        }

        return testResults.size();
    }

    public List<TestResult> getTestResults() {
        if (testResults.isEmpty()) {
            generateTestResults();
        }

        return Collections.unmodifiableList(testResults);
    }

    private void generateTestResults() {
        final long now = System.currentTimeMillis();
        final int monthsBeforeNow = dataFactory.getNumberBetween(4, 10);
        final long start = TimeHelper.addMonths(-1 * monthsBeforeNow, now);

        testResults.clear();
        testResults.addAll(generateDataBetween(start, now));
    }

    @NonNull
    public List<TestResult> generateDataBetween(final long begin, final long end) {
        final List<TestResult> results = new ArrayList<>();
        final int daysBetween = TimeHelper.getDaysBetween(begin, end);

        for (int day = 0; day <= daysBetween; day++) {
            final int numOfMeasurementsThatDay = dataFactory.getNumberUpTo(MAX_MEASUREMENTS_PER_DAY);
            for (int measurement = 0; measurement < numOfMeasurementsThatDay; measurement++) {
                final TestResult result = correctTimestampWhenNecessary(results, generateDataForGivenDay(TimeHelper.addDays(day, begin), null));
                results.add(result);
            }
        }

        return results;
    }

    private TestResult correctTimestampWhenNecessary(final List<TestResult> randomModels, final TestResult result) {
        final boolean hasCollision = Ix.from(randomModels)
                .filter(model -> Math.abs(result.getTimestamp() - model.getTimestamp()) < MIN_TIME_BETWEEN_MEASURES)
                .count()
                .single() > 0;

        return hasCollision
                ? result.toBuilder().setTimestamp(generateRandomTimeAtGivenDay(result.getTimestamp())).build()
                : result;
    }

    private TestResult generateDataForGivenDay(final long timestamp, final String id) {
        return TestResult.builder()
                .setReaderId(dataFactory.getRandomChars(10))
                .setId(id == null ? dataFactory.getRandomChars(10) : id)
                .setPkuLevel(PkuLevel.create(dataFactory.getNumberUpTo((int) Constants.DEFAULT_PKU_HIGHEST_VALUE), PkuLevelUnits.MICRO_MOL))
                .setTimestamp(generateRandomTimeAtGivenDay(timestamp))
                .setSick(dataFactory.chance(SICK_CHANCE))
                .setFasting(dataFactory.chance(FASTING_CHANCE))
                .build();
    }

    private long generateRandomTimeAtGivenDay(final long timestamp) {
        final long earliestTimeAtGivenDay = TimeHelper.getEarliestTimeAtGivenDay(timestamp);
        final long randomOffset = dataFactory.getNumberUpTo((int) DAY_IN_MILLIS);
        return earliestTimeAtGivenDay + randomOffset;
    }

    public TestResult getResult(final String id) {
        final boolean hasResultWithId = Ix.from(testResults).map(TestResult::getId).count().single() == 1;
        if (!hasResultWithId) {
            final TestResult testResult = generateDataForGivenDay(System.currentTimeMillis(), id);
            testResults.add(testResult);
        }

        return Ix.from(testResults).filter(result -> result.getId().equals(id)).single();
    }
}
