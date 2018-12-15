package com.aptatek.pkulab.device.formatter;

import com.aptatek.pkulab.view.main.weekly.chart.ChartEntryData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class WeeklyChartValueFormatter implements IValueFormatter {

    public WeeklyChartValueFormatter() {
    }

    @Override
    public String getFormattedValue(final float value, final Entry entry, final int dataSetIndex, final ViewPortHandler viewPortHandler) {
        if (entry.getData() instanceof ChartEntryData) {
            return ((ChartEntryData) entry.getData()).getLabel();
        }

        return "";
    }
}
