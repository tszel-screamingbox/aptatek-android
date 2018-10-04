package com.aptatek.pkuapp.device.bluetooth.parser;

import com.aptatek.pkuapp.device.bluetooth.CharacteristicReader;
import com.aptatek.pkuapp.device.bluetooth.model.CartridgeIdResponse;
import com.google.gson.Gson;

import javax.inject.Inject;

public class CartridgeIdReader extends CharacteristicReader<CartridgeIdResponse> {

    @Inject
    public CartridgeIdReader(final Gson gson) {
        super(gson);
    }

    @Override
    protected Class<CartridgeIdResponse> getResponseClass() {
        return CartridgeIdResponse.class;
    }
}
