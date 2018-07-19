package com.aptatek.aptatek.view.weekly;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.view.View;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeeklyResultActivity extends BaseActivity<WeeklyResultActivityView, WeeklyResultActivityPresenter> implements WeeklyResultActivityView {

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
    }

    private void initChart() {
        final ArrayList<BubbleEntry> entries = new ArrayList<>();
        entries.add(new BubbleEntry(0, 5, 0.2f));
        entries.add(new BubbleEntry(0, 13, 0.2f));
        entries.add(new BubbleEntry(1, 6, 0.2f));
        entries.add(new BubbleEntry(1, 10, 0.2f));
        entries.add(new BubbleEntry(2, 4, 0.2f));
        entries.add(new BubbleEntry(2, 12, 0.2f));
        entries.add(new BubbleEntry(3, 20, 0.2f));
        entries.add(new BubbleEntry(4, 22, 0.2f));
        entries.add(new BubbleEntry(5, 22, 0.2f));
        entries.add(new BubbleEntry(6, 23, 0.2f));

        final BubbleDataSet dataSet = new BubbleDataSet(entries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        final XAxis xAxis = weeklyBubbleChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getResources().getStringArray(R.array.weekly_days)));
        final YAxis yAxis = weeklyBubbleChart.getAxisLeft();
        final String[] hours = getResources().getStringArray(R.array.weekly_hours);
        yAxis.setValueFormatter(new IndexAxisValueFormatter(hours));
        yAxis.setGranularity(1f); // interval 1
        yAxis.setLabelCount(hours.length);
        weeklyBubbleChart.getAxisLeft().setInverted(true);
        final BubbleData data = new BubbleData(dataSet);
        weeklyBubbleChart.setData(data);
    }
}
