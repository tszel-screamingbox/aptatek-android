package com.aptatek.pkulab.device.bluetooth.characteristics.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.model.TestProgressResponse;
import com.google.gson.Gson;

import javax.inject.Inject;

public class TestProgressReader  extends JsonCharacteristicReader<TestProgressResponse> {

    @Inject
    public TestProgressReader(@NonNull final Gson gson) {
        super(gson);
    }

    @Override
    protected Class<TestProgressResponse> getResponseClass() {
        return TestProgressResponse.class;
    }
}
