package com.aptatek.aptatek.view.main.adapter;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.data.chart.ChartDTO;
import com.aptatek.aptatek.data.chart.Measure;
import com.aptatek.aptatek.util.StringUtils;
import com.aptatek.aptatek.view.base.list.IListTypeProvider;

import java.util.Comparator;
import java.util.List;

import ix.Ix;

public class ChartVM implements IListTypeProvider {

    private long id;
    private String date;
    private CharSequence details;
    private int numberOfMeasures;
    private float bubbleYAxis;
    private float startLineYAxis;
    private float endLineYAxis;
    private float minYAxis;
    private float maxYAxis;


    public ChartVM(final ChartDTO chartDTO) {
        this.id = chartDTO.getId();
        this.date = StringUtils.getFormattedDate(chartDTO.getDate());
        this.details = details(chartDTO.getMeasureList());
        this.numberOfMeasures = chartDTO.getMeasureList().size();
        this.bubbleYAxis = chartDTO.getBubbleYAxis();
        this.startLineYAxis = chartDTO.getStartLineYAxis();
        this.endLineYAxis = chartDTO.getEndLineYAxis();
        this.minYAxis = chartDTO.getMinYAxis();
        this.maxYAxis = chartDTO.getMaxYAxis();
    }

    private CharSequence details(final List<Measure> measureList) {
        if (numberOfMeasures == 0) {
            return null;
        }
        final Comparator<Measure> comp = (p1, p2) -> Integer.compare(p1.getPhenylalanineLevel(), p2.getPhenylalanineLevel());
        final Measure highest = Ix.from(measureList)
                .max(comp)
                .first();

        return StringUtils.highlightWord(
                String.valueOf(highest.getPhenylalanineLevel()),
                String.valueOf(highest.getUnit()));
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

    public float getMinYAxis() {
        return minYAxis;
    }

    public float getMaxYAxis() {
        return maxYAxis;
    }

    public boolean isEmpty() {
        return numberOfMeasures == 0;
    }

    @Override
    public int getLayoutType() {
        return R.layout.bubble_item;
    }
}
