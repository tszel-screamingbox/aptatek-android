package com.aptatek.pkulab.device.bluetooth.characteristics.reader;

import android.support.annotation.NonNull;

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
}
