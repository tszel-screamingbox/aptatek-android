package com.aptatek.aptatek.view.main.adapter;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.respository.chart.ChartDTO;
import com.aptatek.aptatek.domain.respository.chart.Measure;
import com.aptatek.aptatek.util.CalendarUtils;
import com.aptatek.aptatek.util.StringUtils;
import com.aptatek.aptatek.view.base.list.IListTypeProvider;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ix.Ix;

public class ChartVM implements IListTypeProvider {

    private static final String PATTERN = "dd\nMMM";

    private long id;
    private Date date;
    private CharSequence details;
    private int numberOfMeasures;
    private int maxPhenylalanineLevel;
    private float bubbleYAxis;
    private float startLineYAxis;
    private float endLineYAxis;

    public ChartVM(final ChartDTO chartDTO) {
        this.id = chartDTO.getId();
        this.date = chartDTO.getDate();
        this.numberOfMeasures = chartDTO.getMeasureList().size();
        this.details = details(chartDTO.getMeasureList());
        this.bubbleYAxis = chartDTO.getBubbleYAxis();
        this.startLineYAxis = chartDTO.getStartLineYAxis();
        this.endLineYAxis = chartDTO.getEndLineYAxis();
    }

    private CharSequence details(final List<Measure> measureList) {
        if (numberOfMeasures == 0) {
            return null;
        }
        final Comparator<Measure> comp = (p1, p2) -> Integer.compare(p1.getPhenylalanineLevel(), p2.getPhenylalanineLevel());
        final Measure highest = Ix.from(measureList)
                .max(comp)
                .first();
        maxPhenylalanineLevel = highest.getPhenylalanineLevel();

        return StringUtils.highlightWord(
                String.valueOf(highest.getPhenylalanineLevel()),
                String.valueOf(highest.getUnit()) + " mg/dl");
    }

    public int getMaxPhenylalanineLevel() {
        return maxPhenylalanineLevel;
    }

    public long getId() {
        return id;
    }

    public String getFormattedDate() {
        return CalendarUtils.formatDate(date, PATTERN);
    }

    public Date getDate() {
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
        return numberOfMeasures == 0;
    }

    @Override
    public int getLayoutType() {
        return R.layout.bubble_item;
    }
}
