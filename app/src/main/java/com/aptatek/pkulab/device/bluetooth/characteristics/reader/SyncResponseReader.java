package com.aptatek.pkulab.device.bluetooth.characteristics.reader;

import com.aptatek.pkulab.device.bluetooth.model.ResultSyncResponse;
import com.google.gson.Gson;

import javax.inject.Inject;

public class SyncResponseReader extends JsonCharacteristicReader<ResultSyncResponse> {

    @Inject
    public SyncResponseReader(final Gson gson) {
        super(gson);
    }

    @Override
    protected Class<ResultSyncResponse> getResponseClass() {
        return ResultSyncResponse.class;
    }
}
