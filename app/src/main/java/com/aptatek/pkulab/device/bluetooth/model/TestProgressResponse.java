package com.aptatek.pkulab.device.bluetooth.model;

import com.google.gson.annotations.SerializedName;

public class TestProgressResponse {

    private String start;
    private String end;
    @SerializedName("%")
    private int progress;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "TestProgressResponse{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", progress=" + progress +
                '}';
    }
}
