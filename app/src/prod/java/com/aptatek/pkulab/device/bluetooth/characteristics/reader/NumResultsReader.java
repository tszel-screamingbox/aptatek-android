package com.aptatek.pkulab.device.bluetooth.characteristics.reader;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.model.NumPreviousResultsResponse;
import com.google.gson.Gson;

import javax.inject.Inject;

public class NumResultsReader extends JsonCharacteristicReader<NumPreviousResultsResponse> {

    @Inject
    public NumResultsReader(@NonNull final Gson gson) {
        super(gson);
    }

    @Override
    protected Class<NumPreviousResultsResponse> getResponseClass() {
        return NumPreviousResultsResponse.class;
    }
}
