package com.aptatek.pkulab.device.bluetooth.model;

public class ResultResponse {

    private String date;
    private String result;
    private String units;
    private String assay;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getAssay() {
        return assay;
    }

    public void setAssay(String assay) {
        this.assay = assay;
    }

    @Override
    public String toString() {
        return "ResultResponse{" +
                "date='" + date + '\'' +
                ", result='" + result + '\'' +
                ", units='" + units + '\'' +
                ", assay='" + assay + '\'' +
                '}';
    }
}
