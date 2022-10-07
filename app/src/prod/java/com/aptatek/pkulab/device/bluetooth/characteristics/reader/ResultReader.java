package com.aptatek.pkulab.device.bluetooth.characteristics.reader;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.model.ResultResponse;
import com.google.gson.Gson;

import javax.inject.Inject;

public class ResultReader extends JsonCharacteristicReader<ResultResponse> {

    @Inject
    public ResultReader(@NonNull final Gson gson) {
        super(gson);
    }

    @Override
    protected Class<ResultResponse> getResponseClass() {
        return ResultResponse.class;
    }

    @Override
    protected ResultResponse parseFromString(@NonNull String value) {
        final ResultResponse resultResponse = super.parseFromString(value);
        resultResponse.setRawResponse(value);
        return resultResponse;
    }
}
