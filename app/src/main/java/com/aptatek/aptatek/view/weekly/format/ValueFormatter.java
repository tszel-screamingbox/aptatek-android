package com.aptatek.aptatek.view.weekly.format;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.Map;

public class ValueFormatter implements IValueFormatter {

    private final Map<Entry, String> labels;

    public ValueFormatter(final Map<Entry, String> labels) {
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
