package com.aptatek.aptatek.data.model;

public class CubeDataModel {

    private long id;

    private String cubeId;

    private long timestamp;

    private double valueInMMol;

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
}
