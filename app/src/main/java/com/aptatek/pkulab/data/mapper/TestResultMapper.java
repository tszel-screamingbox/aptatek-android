package com.aptatek.pkulab.data.mapper;

import com.aptatek.pkulab.data.model.TestResultDataModel;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.reader.TestResult;

import java.util.List;

import javax.inject.Inject;

import ix.Ix;

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
        return TestResult.builder()
                .setReaderId(dataModel.getReaderId())
                .setId(dataModel.getId())
                .setTimestamp(dataModel.getTimestamp())
                .setPkuLevel(dataModel.getPkuLevel())
                .setValid(dataModel.isValid())
                .build();
    }

    @Override
    public List<TestResultDataModel> mapListToData(final List<TestResult> domainModels) {
        return Ix.from(domainModels)
                .map(this::mapToData)
                .toList();
    }

    @Override
    public TestResultDataModel mapToData(final TestResult domainModel) {
        final TestResultDataModel testResultDataModel = new TestResultDataModel();
        testResultDataModel.setReaderId(domainModel.getReaderId());
        testResultDataModel.setId(domainModel.getId());
        testResultDataModel.setTimestamp(domainModel.getTimestamp());
        testResultDataModel.setPkuLevel(domainModel.getPkuLevel() == null ? PkuLevel.create(0f, PkuLevelUnits.MICRO_MOL) : domainModel.getPkuLevel());
        testResultDataModel.setValid(domainModel.isValid());
        return testResultDataModel;
    }

}
