package com.aptatek.pkulab.device.bluetooth.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ResultResponse {

    @Nullable // remove it as soon as the firmware is updated and will have idx property as the IDC says...
    @SerializedName("idx")
    private String id;
    private String date;
    private String results;
    private String assay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getAssay() {
        return assay;
    }

    public void setAssay(String assay) {
        this.assay = assay;
    }
}
