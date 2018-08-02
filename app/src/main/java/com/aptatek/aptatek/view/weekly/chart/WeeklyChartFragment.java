package com.aptatek.aptatek.view.weekly.chart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.FragmentComponent;
import com.aptatek.aptatek.view.base.BaseFragment;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import javax.inject.Inject;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import butterknife.BindView;


public class WeeklyChartFragment extends BaseFragment implements WeeklyChartView {

    private static final float MIN_X = -0.5f;
    private static final float MAX_X = 6.5f;
    private static final float OFFSET = 2f;

    @Arg
    int weekBefore;

    @Inject
    WeeklyChartPresenter presenter;

    @BindView(R.id.weeklyChart)
    BubbleChart weeklyBubbleChart;

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weekly_chart;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStarter.fill(this, savedInstanceState);
    }

    @Override
    protected void initObjects(final View view) {
        initChart();
        if (weekBefore >= 0) {
            weeklyBubbleChart.getData().addDataSet(presenter.getChartData(weekBefore));
            weeklyBubbleChart.invalidate();
        }
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @NonNull
    @Override
    public WeeklyChartPresenter createPresenter() {
        return presenter;
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

        final BubbleData data = new BubbleData();
        weeklyBubbleChart.setData(data);
        weeklyBubbleChart.getAxisRight().setEnabled(false);
        weeklyBubbleChart.getAxisRight().setDrawGridLines(false);
        weeklyBubbleChart.setDrawBorders(false);
        weeklyBubbleChart.setScaleEnabled(false);
        weeklyBubbleChart.getLegend().setEnabled(false);
        weeklyBubbleChart.getDescription().setEnabled(false);
    }
}
