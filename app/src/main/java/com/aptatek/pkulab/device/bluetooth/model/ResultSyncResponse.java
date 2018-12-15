package com.aptatek.pkulab.device.bluetooth.model;

import java.util.List;

public class ResultSyncResponse {

    private int numberOfResults;
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
