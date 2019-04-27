package com.aptatek.pkulab.device.bluetooth.model;

import com.google.gson.annotations.SerializedName;

public class SyncAfterRequest {

    @SerializedName("after")
    private String after;

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    @Override
    public String toString() {
        return "SyncAfterRequest{" +
                "after='" + after + '\'' +
                '}';
    }
}
