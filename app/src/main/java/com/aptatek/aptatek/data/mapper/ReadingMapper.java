package com.aptatek.aptatek.data.mapper;

import com.aptatek.aptatek.data.model.ReadingDataModel;
import com.aptatek.aptatek.domain.model.Reading;

import java.util.List;

import javax.inject.Inject;

import ix.Ix;

public class ReadingMapper {

    @Inject
    ReadingMapper() {

    }

    public Reading toDomain(ReadingDataModel readingDataModel) {
        return Reading.builder()
                .setId(readingDataModel.getId())
                .setInMicroMol(readingDataModel.getMicroMolValue())
                .setInMilligramPerDeciliter(readingDataModel.getMgPerDlValue())
                .setTimestamp(readingDataModel.getTimestamp())
                .build();
    }

    public ReadingDataModel toData(Reading reading) {
        final ReadingDataModel readingDataModel = new ReadingDataModel();
        if (reading.getId() != null) {
            readingDataModel.setId(reading.getId());
        }
        readingDataModel.setMgPerDlValue(reading.getInMilligramPerDeciliter());
        readingDataModel.setMicroMolValue(reading.getInMicroMol());
        readingDataModel.setTimestamp(reading.getTimestamp());

        return readingDataModel;
    }

    public List<Reading> toDomainList(List<ReadingDataModel> readingDataModels) {
        return Ix.from(readingDataModels).map(this::toDomain).toList();
    }

    public List<ReadingDataModel> toDataList(List<Reading> readings) {
        return Ix.from(readings).map(this::toData).toList();
    }

}
