package com.aptatek.pkulab.device.bluetooth.model;

import com.aptatek.pkulab.device.bluetooth.model.json.ResultSyncAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultSyncResponse {

    @SerializedName("nResults")
    private int numberOfResults;
    @JsonAdapter(ResultSyncAdapter.class)
    private List<String> identifiers;

    public int getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(int numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<String> identifiers) {
        this.identifiers = identifiers;
    }
}
