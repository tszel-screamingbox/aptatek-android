package com.aptatek.pkulab.device.bluetooth.mapper;

import com.aptatek.pkulab.device.bluetooth.model.TestProgressResponse;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.model.reader.TestProgress;

import java.util.List;

import javax.inject.Inject;

import ix.Ix;

public class TestProgressMapper implements Mapper<TestProgress, TestProgressResponse> {

    @Inject
    public TestProgressMapper() {
    }

    @Override
    public List<TestProgress> mapListToDomain(final List<TestProgressResponse> dataModels) {
        return Ix.from(dataModels).map(this::mapToDomain).toList();
    }

    @Override
    public TestProgress mapToDomain(final TestProgressResponse dataModel) {
        return TestProgress.create(
                DateParser.tryParseDate(dataModel.getStart()),
                DateParser.tryParseDate(dataModel.getEnd()),
                dataModel.getProgress()
        );
    }

    @Override
    public List<TestProgressResponse> mapListToData(final List<TestProgress> domainModels) {
        return Ix.from(domainModels).map(this::mapToData).toList();
    }

    @Override
    public TestProgressResponse mapToData(final TestProgress domainModel) {
        return new TestProgressResponse(); // not used in this direction
    }
}