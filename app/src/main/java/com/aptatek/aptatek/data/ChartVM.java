package com.aptatek.aptatek.data;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.view.base.list.IListTypeProvider;

import java.util.Random;

public class ChartVM implements IListTypeProvider {

    private long id;

    private String date;
    private int maxPhenylalanineLevel;
    private String unit;
    private int numberOfMeasures;

    private float bubbleYAxis;
    private float startLineYAxis;
    private float endLineYAxis;


    public ChartVM(final CubeData cubeData) {
        initFromDB(cubeData);
    }

    private void initFromDB(final CubeData cubeData) {
        this.id = 0;
        this.date = "1\nJan";
        this.maxPhenylalanineLevel = 420;
        this.unit = "5.6 mg/dl";
        this.numberOfMeasures = 2;

        Random r = new Random();
        int i = r.nextInt(100);
        this.bubbleYAxis = (float) i / 100;
        this.startLineYAxis = 80;
        this.endLineYAxis = 80;
    }

    public long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getMaxPhenylalanineLevel() {
        return maxPhenylalanineLevel;
    }

    public String getUnit() {
        return unit;
    }

    public int getNumberOfMeasures() {
        return numberOfMeasures;
    }

    public float getBubbleYAxis() {
        return bubbleYAxis;
    }

    public float getStartLineYAxis() {
        return startLineYAxis;
    }

    public float getEndLineYAxis() {
        return endLineYAxis;
    }

    @Override
    public int getLayoutType() {
        return R.layout.bubble_item;
    }
}
