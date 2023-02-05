package com.aptatek.pkulab.data.mapper;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.data.model.TestResultDataModel;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.reader.TestResult;

import java.util.List;

import javax.inject.Inject;

import ix.Ix;
import timber.log.Timber;

public class TestResultMapper implements Mapper<TestResult, TestResultDataModel> {

    @Inject
    TestResultMapper() {
    }

    @Override
    public List<TestResult> mapListToDomain(final List<TestResultDataModel> dataModels) {
        return Ix.from(dataModels)
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public TestResult mapToDomain(final TestResultDataModel dataModel) {
        final TestResult result = TestResult.builder()
                .setReaderId(dataModel.getReaderId())
                .setId(dataModel.getId())
                .setReaderMac(dataModel.getReaderMac())
                .setTimestamp(dataModel.getTimestamp())
                .setPkuLevel(parsePkuLevel(dataModel))
                .setValid(dataModel.isValid())
                .setConfigHash(dataModel.getConfigHash())
                .setFirmwareVersion(dataModel.getFirmwareVersion())
                .setSoftwareVersion(dataModel.getSoftwareVersion())
                .setHardwareVersion(dataModel.getHardwareVersion())
                .setCassetteLot(dataModel.getCassetteLot())
                .setOverallResult(dataModel.getOverallResult())
                .setHumidity(dataModel.getHumidity())
                .setTemperature(dataModel.getTemperature())
                .setEndTimestamp(dataModel.getEndTimestamp())
                .setAssayHash(dataModel.getAssayHash())
                .setAssayVersion(dataModel.getAssayVersion())
                .setAssay(dataModel.getAssay())
                .setRawResponse(dataModel.getRawResponse())
                .setCassetteExpiry(dataModel.getCassetteExpiry())
                .setReaderMode(dataModel.getReaderMode())
                .build();

        return result;
    }

    @Override
    public List<TestResultDataModel> mapListToData(final List<TestResult> domainModels) {
        return Ix.from(domainModels)
                .map(this::mapToData)
                .toList();
    }

    @Override
    public TestResultDataModel mapToData(final TestResult domainModel) {
        final PkuLevel pkuLevel = domainModel.getPkuLevel() == null ? PkuLevel.create(0f, PkuLevelUnits.MICRO_MOL) : domainModel.getPkuLevel();
        final TestResultDataModel testResultDataModel = new TestResultDataModel();
        testResultDataModel.setReaderId(domainModel.getReaderId());
        testResultDataModel.setReaderMac(domainModel.getReaderMac());
        testResultDataModel.setId(domainModel.getId());
        testResultDataModel.setTimestamp(domainModel.getTimestamp());
        testResultDataModel.setNumericValue(pkuLevel.getValue());
        testResultDataModel.setUnit(pkuLevel.getUnit().name());
        testResultDataModel.setTextResult(pkuLevel.getTextResult());
        testResultDataModel.setValid(domainModel.isValid());
        testResultDataModel.setAssay(domainModel.getAssay());
        testResultDataModel.setCassetteLot(domainModel.getCassetteLot() == null ? -1L : domainModel.getCassetteLot());
        testResultDataModel.setOverallResult(domainModel.getOverallResult());
        testResultDataModel.setEndTimestamp(domainModel.getEndTimestamp());
        testResultDataModel.setAssayHash(domainModel.getAssayHash());
        testResultDataModel.setAssayVersion(domainModel.getAssayVersion());
        testResultDataModel.setConfigHash(domainModel.getConfigHash());
        testResultDataModel.setHumidity(domainModel.getHumidity());
        testResultDataModel.setTemperature(domainModel.getTemperature());
        testResultDataModel.setFirmwareVersion(domainModel.getFirmwareVersion());
        testResultDataModel.setHardwareVersion(domainModel.getHardwareVersion());
        testResultDataModel.setSoftwareVersion(domainModel.getSoftwareVersion());
        testResultDataModel.setRawResponse(domainModel.getRawResponse());
        testResultDataModel.setReaderMode(domainModel.getReaderMode());
        testResultDataModel.setCassetteExpiry(domainModel.getCassetteExpiry());
        return testResultDataModel;
    }

    @Nullable
    private PkuLevel parsePkuLevel(final TestResultDataModel dataModel) {
        try {
            final float value = dataModel.getNumericValue();
            final PkuLevelUnits unit = parseUnit(dataModel.getUnit());
            return PkuLevel.builder()
                    .setValue(value)
                    .setUnit(unit)
                    .setTextResult(dataModel.getTextResult())
                    .build();
        } catch (Exception ex) {
            Timber.d("Failed to parse pkuLevel from data model: %s", dataModel);
            return PkuLevel.builder()
                    .setValue(-1f)
                    .setUnit(PkuLevelUnits.MABS)
                    .build();
        }
    }

    private PkuLevelUnits parseUnit(final String units) {
        for (PkuLevelUnits value : PkuLevelUnits.values()) {
            if (value.name().equals(units)) {
                return value;
            }
        }

        Timber.d("Failed to parse pkuLevelUnit from data model unit: %s", units);
        return PkuLevelUnits.MABS;
    }

}

