package com.aptatek.aptatek.device.formatter;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.Map;

public class WeeklyChartValueFormatter implements IValueFormatter {

    private final Map<Entry, String> labels;

    public WeeklyChartValueFormatter(final Map<Entry, String> labels) {
        this.labels = labels;
    }

    @Override
    public String getFormattedValue(final float value, final Entry entry, final int dataSetIndex, final ViewPortHandler viewPortHandler) {
        final String label = labels.get(entry);
        if (label == null) {
            return "";
        }
        return label;
    }
}
