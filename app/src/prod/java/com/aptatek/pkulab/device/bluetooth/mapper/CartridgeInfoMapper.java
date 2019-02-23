package com.aptatek.pkulab.device.bluetooth.mapper;

import com.aptatek.pkulab.device.bluetooth.model.CartridgeIdResponse;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.model.reader.CartridgeInfo;

import java.util.List;

import javax.inject.Inject;

import ix.Ix;

public class CartridgeInfoMapper implements Mapper<CartridgeInfo, CartridgeIdResponse> {

    @Inject
    public CartridgeInfoMapper() {
    }

    @Override
    public List<CartridgeInfo> mapListToDomain(final List<CartridgeIdResponse> dataModels) {
        return Ix.from(dataModels).map(this::mapToDomain).toList();
    }

    @Override
    public CartridgeInfo mapToDomain(final CartridgeIdResponse dataModel) {
        return CartridgeInfo.builder()
                .setExpiry(DateParser.tryParseDate(dataModel.getExpiry()))
                .setDate(DateParser.tryParseDate(dataModel.getDate()))
                .setCalibration(dataModel.getCalibration())
                .setLot(dataModel.getLot())
                .setType(dataModel.getType())
                .build();
    }

    @Override
    public List<CartridgeIdResponse> mapListToData(final List<CartridgeInfo> domainModels) {
        return Ix.from(domainModels).map(this::mapToData).toList();
    }

    @Override
    public CartridgeIdResponse mapToData(final CartridgeInfo domainModel) {
        return new CartridgeIdResponse(); // TODO not used
    }
}
