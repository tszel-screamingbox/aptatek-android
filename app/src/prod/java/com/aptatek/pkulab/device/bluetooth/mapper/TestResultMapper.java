package com.aptatek.pkulab.device.bluetooth.mapper;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.device.bluetooth.model.ResultResponse;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.reader.TestResult;

import java.util.List;
import java.util.Locale;

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
        final TestResult result = TestResult.builder()
                .setId(dataModel.getDate())
                .setPkuLevel(parsePkuLevel(dataModel))
                .setTimestamp(DateParser.tryParseDate(dataModel.getDate()))
                .setEndTimestamp(DateParser.tryParseDate(dataModel.getEndDate()))
                .setReaderId(dataModel.getProductSerial())
                .setValid(dataModel.isValid())
                .setAssay(dataModel.getAssay())
                .setAssayVersion(dataModel.getAssayVersion())
                .setAssayHash(dataModel.getAssayHash())
                .setTemperature(dataModel.getTemperature())
                .setHumidity(dataModel.getHumidity())
                .setOverallResult(dataModel.getOverallResult())
                .setCassetteLot(dataModel.getCassette() == null ? -1L : dataModel.getCassette().getLot() == null ? -1L : dataModel.getCassette().getLot())
                .setHardwareVersion(dataModel.getHardwareVersion())
                .setSoftwareVersion(dataModel.getSoftwareVersion())
                .setFirmwareVersion(dataModel.getFirmwareVersion())
                .setConfigHash(dataModel.getConfigHash())
                .setReaderMode(dataModel.getMode())
                .setCassetteExpiry(tryParseCassetteExp(dataModel))
                .setRawResponse(dataModel.getRawResponse())
                .build();
        return  result;
    }

    private long tryParseCassetteExp(final ResultResponse model) {
        long exp = -1L;
        if (model.getCassette() != null) {
            try {
                exp = Long.parseLong(model.getCassette().getExpiry());
            } catch (Exception ex) {
                Timber.d("Failed to parse cassette exp from: %s", model);
            }
        }
        return exp;
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
            return PkuLevel.builder()
                    .setValue(value)
                    .setUnit(unit)
                    .setAssayName(resultData.getAssayName())
                    .setName(resultData.getName())
                    .setTextResult(resultData.getTextResult())
                    .build();
        } catch (Exception ex) {
            Timber.d("Failed to parse pkuLevel from result response: %s", resultResponse);
            return PkuLevel.builder()
                    .setValue(0f)
                    .setUnit(PkuLevelUnits.MABS)
                    .build();
        }
    }

    private PkuLevelUnits parseUnit(final String units) {
        switch (units.toLowerCase(Locale.getDefault())) {
            case "umol/l": return PkuLevelUnits.MICRO_MOL;
            case "mabs": return PkuLevelUnits.MABS;
            default: throw new IllegalArgumentException("Unhandled unit received: " + units);
        }
    }

}
