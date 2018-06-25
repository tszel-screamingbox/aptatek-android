package com.aptatek.aptatek.data;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.view.base.list.IListTypeProvider;
import com.aptatek.aptatek.view.chart.StringUtils;

import java.util.Random;

public class ChartVM implements IListTypeProvider {

    private long id;
    private String date;
    private CharSequence details;
    private int numberOfMeasures;
    private float bubbleYAxis;
    private float startLineYAxis;
    private float endLineYAxis;
    private boolean isEmpty = false;


    public ChartVM(final CubeData cubeData) {
        initFromDB(cubeData);
    }

    private void initFromDB(final CubeData cubeData) {
        this.id = 0;
        this.date = "1\nJan";
        this.details = StringUtils.highlightWord("450", "35,6 mg/dl");
        this.numberOfMeasures = 2;

        Random r = new Random();
        int i = r.nextInt(100);
        this.bubbleYAxis = (float) i / 100;

        if (i < 50) {
            isEmpty = true;
        }

        this.startLineYAxis = 80;
        this.endLineYAxis = 80;
    }

    public long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public CharSequence getDetails() {
        return details;
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

    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public int getLayoutType() {
        return R.layout.bubble_item;
    }
}
