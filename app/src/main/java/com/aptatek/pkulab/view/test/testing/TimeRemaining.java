package com.aptatek.pkulab.view.test.testing;

public class TimeRemaining {

    private final String timeRemaining;
    private final int progressPercent;

    public TimeRemaining(String timeRemaining, int progressPercent) {
        this.timeRemaining = timeRemaining;
        this.progressPercent = progressPercent;
    }

    public String getTimeRemaining() {
        return timeRemaining;
    }

    public int getProgressPercent() {
        return progressPercent;
    }
}
