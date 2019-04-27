package com.aptatek.pkulab.device.bluetooth.model;

import com.google.gson.annotations.SerializedName;

public class RequestResultRequest {

    @SerializedName("reqResultID")
    private String resultId;

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }
}
