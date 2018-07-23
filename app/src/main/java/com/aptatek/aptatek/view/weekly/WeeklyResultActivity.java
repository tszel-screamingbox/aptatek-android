package com.aptatek.aptatek.view.weekly;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.view.View;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.weekly.format.ValueFormatter;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeeklyResultActivity extends BaseActivity<WeeklyResultActivityView, WeeklyResultActivityPresenter> implements WeeklyResultActivityView {

    private static final float MIN_X = -0.5f;
    private static final float MAX_X = 6.5f;
    private static final float OFFSET = 2f;

    @Inject
    WeeklyResultActivityPresenter presenter;

    @BindView(R.id.emptyGroup)
    Group emptyGroup;

    @BindView(R.id.weeklyChart)
    BubbleChart weeklyBubbleChart;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);
        ButterKnife.bind(this);
        initChart();
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @NonNull
    @Override
    public WeeklyResultActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return 0;
    }

    @OnClick(R.id.playIcon)
    public void onPlayButtonClicked() {
        emptyGroup.setVisibility(View.GONE);

        addEntry();
    }

    private void addEntry() {
        final BubbleData data = weeklyBubbleChart.getBubbleData();

        final ArrayList<BubbleEntry> entries = new ArrayList<>();
        entries.add(new BubbleEntry(0, 0, 0.1f));
        entries.add(new BubbleEntry(0, 13, 0.1f));
        entries.add(new BubbleEntry(1, 7, 0.1f));
        entries.add(new BubbleEntry(1, 10, 0.1f));
        entries.add(new BubbleEntry(2, 8, 0.1f));
        entries.add(new BubbleEntry(2, 12, 0.1f));
        entries.add(new BubbleEntry(3, 20, 0.1f));
        entries.add(new BubbleEntry(4, 23, 0.1f));
//        entries.add(new BubbleEntry(5, 22, 0.1f));
        entries.add(new BubbleEntry(6, 23, 0.1f));
        entries.add(new BubbleEntry(6, 11, 0.1f));


        final BubbleDataSet dataSet = new BubbleDataSet(entries, null);
        dataSet.setColors(Color.RED, Color.GRAY, Color.GREEN);
        data.addDataSet(dataSet);
        final Map<Entry, String> labels = new HashMap<>();
        for (final BubbleEntry bubbleEntry : entries) {
            labels.put(bubbleEntry, "123");
        }
        dataSet.setValueFormatter(new ValueFormatter(labels));

        data.notifyDataChanged();
        weeklyBubbleChart.invalidate();
    }

    private void initChart() {
        final XAxis xAxis = weeklyBubbleChart.getXAxis();
        final String[] days = getResources().getStringArray(R.array.weekly_days);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(MIN_X);
        xAxis.setAxisMaximum(MAX_X);
        final YAxis yAxis = weeklyBubbleChart.getAxisLeft();
        final String[] hours = getResources().getStringArray(R.array.weekly_hours);
        yAxis.setValueFormatter(new IndexAxisValueFormatter(hours));
        yAxis.setLabelCount(hours.length);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setInverted(true);
        yAxis.setAxisMinimum(-OFFSET);
        yAxis.setAxisMaximum(hours.length + OFFSET);
        weeklyBubbleChart.getAxisRight().setEnabled(false);
        weeklyBubbleChart.getAxisRight().setDrawGridLines(false);
        weeklyBubbleChart.setDrawBorders(false);
        weeklyBubbleChart.setScaleEnabled(false);
        weeklyBubbleChart.getDescription().setEnabled(false);
        final BubbleData data = new BubbleData();
        weeklyBubbleChart.setData(data);
    }
}
