package com.aptatek.pkulab.device.bluetooth.mapper;

import com.aptatek.pkulab.device.bluetooth.model.ErrorResponse;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.model.reader.Error;

import java.util.List;

import javax.inject.Inject;

import ix.Ix;

public class ErrorMapper implements Mapper<Error,ErrorResponse> {

    @Inject
    public ErrorMapper() {
    }

    @Override
    public List<Error> mapListToDomain(final List<ErrorResponse> dataModels) {
        return Ix.from(dataModels).map(this::mapToDomain).toList();
    }

    @Override
    public Error mapToDomain(final ErrorResponse dataModel) {
        return Error.create(dataModel.getError());
    }

    @Override
    public List<ErrorResponse> mapListToData(final List<Error> domainModels) {
        return Ix.from(domainModels).map(this::mapToData).toList();
    }

    @Override
    public ErrorResponse mapToData(final Error domainModel) {
        return new ErrorResponse(); // not used anyway
    }
}
