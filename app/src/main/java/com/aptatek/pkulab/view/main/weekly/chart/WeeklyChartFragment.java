package com.aptatek.pkulab.view.main.weekly.chart;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.lang.reflect.Field;

import javax.inject.Inject;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import butterknife.BindView;
import timber.log.Timber;


public class WeeklyChartFragment extends BaseFragment implements WeeklyChartView {

    private static final float MIN_X = -0.5f;
    private static final float MAX_X = 6.5f;
    private static final int Y_PADDING = 2;

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
            presenter.getChartData(weekBefore);
        }
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.plus(new RangeInfoModule(), new ChartModule())
                .inject(this);
    }

    @NonNull
    @Override
    public WeeklyChartPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void displayChartData(final BubbleDataSet bubbleDataSet) {
        weeklyBubbleChart.getData().addDataSet(bubbleDataSet);
        weeklyBubbleChart.invalidate();
    }

    private void initChart() {
        final Typeface typeface = ResourcesCompat.getFont(getBaseActivity().getApplicationContext(), R.font.nunito_black);
        final XAxis xAxis = weeklyBubbleChart.getXAxis();
        final String[] days = getResources().getStringArray(R.array.weekly_days);
        xAxis.setTextColor(getResources().getColor(R.color.applicationSolidGray));
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(MIN_X);
        xAxis.setAxisMaximum(MAX_X);
        xAxis.setTypeface(typeface);

        final YAxis yAxis = weeklyBubbleChart.getAxisLeft();
        final String[] hours = getResources().getStringArray(R.array.weekly_hours);
        yAxis.setValueFormatter((value, axis) -> {
            final int round = Math.round(value);
            final int index = Math.round(round / (float) Constants.ONE_HOUR_IN_MINUTES);

            return hours[index + Y_PADDING];
        });
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setInverted(true);
        yAxis.setAxisMinimum(-1 * Y_PADDING * Constants.ONE_HOUR_IN_MINUTES);
        yAxis.setAxisMaximum(Constants.ONE_DAY_IN_HOURS * Constants.ONE_HOUR_IN_MINUTES + (Y_PADDING * Constants.ONE_HOUR_IN_MINUTES));
        yAxis.setLabelCount(hours.length, true);

        // need to use reflection since the Axis.setLabelCount sets 25 as max value...
        try {
            final Field mLabelCount = yAxis.getClass().getSuperclass().getDeclaredField("mLabelCount");
            mLabelCount.setAccessible(true);
            mLabelCount.set(yAxis, hours.length);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Timber.d("Failed to set mLabelCount");
        }

        yAxis.setTextColor(getResources().getColor(R.color.applicationSolidGray));
        yAxis.setTypeface(typeface);

        final BubbleData data = new BubbleData();
        weeklyBubbleChart.setRendererLeftYAxis(new CustomYAxisRenderer(weeklyBubbleChart));
        weeklyBubbleChart.setData(data);
        weeklyBubbleChart.getAxisRight().setEnabled(false);
        weeklyBubbleChart.getAxisRight().setDrawGridLines(false);
        weeklyBubbleChart.setDrawBorders(false);
        weeklyBubbleChart.setScaleEnabled(false);
        weeklyBubbleChart.getLegend().setEnabled(false);
        weeklyBubbleChart.getDescription().setEnabled(false);
        weeklyBubbleChart.setRenderer(new CustomBubbleChartRenderer(weeklyBubbleChart));
    }

}
