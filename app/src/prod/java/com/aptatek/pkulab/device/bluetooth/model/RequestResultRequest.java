package com.aptatek.pkulab.device.bluetooth.model;

import com.google.gson.annotations.SerializedName;

public class RequestResultRequest {

    @SerializedName("reqResultID") // TODO check with Lumos because it has 2 names in the ICD...
    private String resultId;

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }
}
