package com.aptatek.pkulab.device.bluetooth.mapper;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.device.bluetooth.model.ResultResponse;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.reader.TestResult;

import java.util.List;

import javax.inject.Inject;

import ix.Ix;
import timber.log.Timber;

public class TestResultMapper implements Mapper<TestResult, ResultResponse> {

    @Inject
    public TestResultMapper() {
    }

    @Override
    public List<TestResult> mapListToDomain(final List<ResultResponse> dataModels) {
        return Ix.from(dataModels).map(this::mapToDomain).toList();
    }

    @Override
    public TestResult mapToDomain(final ResultResponse dataModel) {
        return TestResult.builder()
                .setId(dataModel.getDate())
                .setPkuLevel(parsePkuLevel(dataModel))
                .setTimestamp(DateParser.tryParseDate(dataModel.getDate()))
                .setReaderId("dummy")
                .build();
    }

    @Override
    public List<ResultResponse> mapListToData(final List<TestResult> domainModels) {
        return Ix.from(domainModels).map(this::mapToData).toList();
    }

    @Override
    public ResultResponse mapToData(final TestResult domainModel) {
        return new ResultResponse(); // won't be used in this way
    }

    @Nullable
    private PkuLevel parsePkuLevel(final ResultResponse resultResponse) {
        try {
            final ResultResponse.ResultData resultData = resultResponse.getResult().get(0);
            final float value = resultData.getNumericalResult();
            final PkuLevelUnits unit = parseUnit(resultData.getUnits());
            return PkuLevel.create(value, unit);
        } catch (Exception ex) {
            Timber.d("Failed to parse pkuLevel from result response: %s", resultResponse);
            return PkuLevel.create(0, PkuLevelUnits.MICRO_MOL);
        }
    }

    private PkuLevelUnits parseUnit(final String units) {
        if ("umol/L".equals(units)) {
            return PkuLevelUnits.MICRO_MOL;
        }

        throw new IllegalArgumentException("Unhandled unit received: " + units);
    }

}
