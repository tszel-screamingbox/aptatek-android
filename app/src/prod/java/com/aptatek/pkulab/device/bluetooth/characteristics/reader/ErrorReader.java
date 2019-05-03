package com.aptatek.pkulab.device.bluetooth.characteristics.reader;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.model.ErrorResponse;
import com.google.gson.Gson;

import javax.inject.Inject;

public class ErrorReader extends JsonCharacteristicReader<ErrorResponse> {

    @Inject
    public ErrorReader(@NonNull final Gson gson) {
        super(gson);
    }

    @Override
    protected Class<ErrorResponse> getResponseClass() {
        return ErrorResponse.class;
    }
}
