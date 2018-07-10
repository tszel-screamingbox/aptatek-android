package com.aptatek.aptatek.domain.respository.chart;

public class Measure {

    private final int phenylalanineLevel;
    private final double unit;


    public Measure(final int phenylalanineLevel, final double unit) {
        this.phenylalanineLevel = phenylalanineLevel;
        this.unit = unit;
    }

    public int getPhenylalanineLevel() {
        return phenylalanineLevel;
    }

    public double getUnit() {
        return unit;
    }
}
