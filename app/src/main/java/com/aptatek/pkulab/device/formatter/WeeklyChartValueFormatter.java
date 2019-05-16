package com.aptatek.pkulab.device.formatter;

import com.aptatek.pkulab.view.main.weekly.chart.ChartEntryData;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class WeeklyChartValueFormatter extends ValueFormatter {

    public WeeklyChartValueFormatter() {
    }

    @Override
    public String getBubbleLabel(final BubbleEntry entry) {
        if (entry.getData() instanceof ChartEntryData) {
            return ((ChartEntryData) entry.getData()).getLabel();
        }

        return "";
    }

    @Override
    public String getFormattedValue(final float value) {
        return "";
    }
}
