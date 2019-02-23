package com.aptatek.pkulab.device.bluetooth.model;

import com.google.gson.annotations.SerializedName;

public class NumPreviousResultsResponse {

    @SerializedName("nResults")
    private int numberOfResults;

    public int getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(int numberOfResults) {
        this.numberOfResults = numberOfResults;
    }
}
