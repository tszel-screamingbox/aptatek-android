package com.aptatek.pkulab.device.bluetooth.characteristics.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.model.WorkflowStateResponse;
import com.google.gson.Gson;

import javax.inject.Inject;

public class WorkflowStateReader extends JsonCharacteristicReader<WorkflowStateResponse> {

    @Inject
    public WorkflowStateReader(@NonNull final Gson gson) {
        super(gson);
    }

    @Override
    protected Class<WorkflowStateResponse> getResponseClass() {
        return WorkflowStateResponse.class;
    }
}
