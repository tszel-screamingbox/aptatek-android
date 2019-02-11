package com.aptatek.pkulab.device.bluetooth.model;

public class CartridgeIdResponse {

    private String expiry;
    private String calibration;
    private String date;
    private String lot;
    private String type;

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCalibration() {
        return calibration;
    }

    public void setCalibration(String calibration) {
        this.calibration = calibration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CartridgeIdResponse{" +
                "expiry='" + expiry + '\'' +
                ", calibration='" + calibration + '\'' +
                ", date='" + date + '\'' +
                ", lot='" + lot + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
