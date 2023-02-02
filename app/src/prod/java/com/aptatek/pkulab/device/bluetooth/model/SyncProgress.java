package com.aptatek.pkulab.device.bluetooth.model;

public class SyncProgress {

    private final int currentResults;
    private final int failedResults;
    private final int totalResults;

    public SyncProgress(int currentResults, int failedResults, int totalResults) {
        this.currentResults = currentResults;
        this.failedResults = failedResults;
        this.totalResults = totalResults;
    }

    public int getCurrentResults() {
        return currentResults;
    }

    public int getFailedResults() {
        return failedResults;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public SyncProgress increaseProgress() {
        return new SyncProgress(currentResults + 1, failedResults, totalResults);
    }

    public SyncProgress failed() {
        return new SyncProgress(currentResults, failedResults + 1, totalResults);
    }

    @Override
    public String toString() {
        return "SyncProgress{" +
                "currentResults=" + currentResults +
                ", failedResults=" + failedResults +
                ", totalResults=" + totalResults +
                '}';
    }
}
