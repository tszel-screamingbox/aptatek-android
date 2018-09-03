package com.aptatek.pkuapp.data.model;

public class CubeDataModel {

    private long id;

    private String cubeId;

    private long timestamp;

    private double valueInMMol;

    private boolean sick;

    private boolean fasting;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getCubeId() {
        return cubeId;
    }

    public void setCubeId(final String cubeId) {
        this.cubeId = cubeId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValueInMMol() {
        return valueInMMol;
    }

    public void setValueInMMol(final double valueInMMol) {
        this.valueInMMol = valueInMMol;
    }

    public boolean isSick() {
        return sick;
    }

    public void setSick(boolean sick) {
        this.sick = sick;
    }

    public boolean isFasting() {
        return fasting;
    }

    public void setFasting(boolean fasting) {
        this.fasting = fasting;
    }
}
