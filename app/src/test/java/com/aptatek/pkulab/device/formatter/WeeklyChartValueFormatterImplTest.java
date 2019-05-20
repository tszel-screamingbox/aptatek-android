package com.aptatek.pkulab.device.formatter;

import com.aptatek.pkulab.view.main.weekly.chart.ChartEntryData;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;

import org.junit.Before;
import org.junit.Test;

public class WeeklyChartValueFormatterImplTest {

    private WeeklyChartValueFormatter formatter;

    @Before
    public void setUp() throws Exception {
        formatter = new WeeklyChartValueFormatter();
    }

    @Test
    public void testFormat() throws Exception {
        final String value1 = formatter.getBubbleLabel(new BubbleEntry(0f, 0f, 0f));
        assert value1.equals("");

        final ChartEntryData chartEntryData = ChartEntryData.builder()
                .setY(0f)
                .setBubbleColor(0)
                .setFasting(false)
                .setSick(false)
                .setLabel("dummy")
                .setLabelColor(0)
                .setSize(0f)
                .setX(0f).build();
        final String formatted2 = formatter.getBubbleLabel(new BubbleEntry(0f, 0f, 0f, chartEntryData));
        assert formatted2.equals("dummy");
    }

}
