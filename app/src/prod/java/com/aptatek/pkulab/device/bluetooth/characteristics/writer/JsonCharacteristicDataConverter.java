package com.aptatek.pkulab.device.bluetooth.characteristics.writer;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import javax.inject.Inject;

public class JsonCharacteristicDataConverter extends CharacteristicDataConverter {

    private final Gson gson;

    @Inject
    public JsonCharacteristicDataConverter(final Gson gson) {
        this.gson = gson;
    }

    @Override
    public byte[] convertData(@NonNull Object data) {
        return gson.toJson(data).getBytes();
    }
}
