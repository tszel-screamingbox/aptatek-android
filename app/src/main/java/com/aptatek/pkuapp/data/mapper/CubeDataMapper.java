package com.aptatek.pkuapp.data.mapper;

import com.aptatek.pkuapp.data.model.CubeDataModel;
import com.aptatek.pkuapp.domain.base.Mapper;
import com.aptatek.pkuapp.domain.model.CubeData;
import com.aptatek.pkuapp.domain.model.PkuLevel;
import com.aptatek.pkuapp.domain.model.PkuLevelUnits;

import java.util.List;

import javax.inject.Inject;

import ix.Ix;

public class CubeDataMapper implements Mapper<CubeData,CubeDataModel> {

    @Inject
    CubeDataMapper() {
    }

    @Override
    public List<CubeData> mapListToDomain(final List<CubeDataModel> dataModels) {
        return Ix.from(dataModels)
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public CubeData mapToDomain(final CubeDataModel dataModel) {
        return CubeData.builder()
                .setCubeId(dataModel.getCubeId())
                .setId(dataModel.getId())
                .setTimestamp(dataModel.getTimestamp())
                .setPkuLevel(PkuLevel.create((float) dataModel.getValueInMMol(), PkuLevelUnits.MICRO_MOL))
                .build();
    }

    @Override
    public List<CubeDataModel> mapListToData(final List<CubeData> domainModels) {
        return Ix.from(domainModels)
                .map(this::mapToData)
                .toList();
    }

    @Override
    public CubeDataModel mapToData(final CubeData domainModel) {
        final CubeDataModel cubeDataModel = new CubeDataModel();
        cubeDataModel.setCubeId(domainModel.getCubeId());
        cubeDataModel.setId(domainModel.getId());
        cubeDataModel.setTimestamp(domainModel.getTimestamp());
        cubeDataModel.setValueInMMol(domainModel.getPkuLevel().getValue());
        return cubeDataModel;
    }

}
