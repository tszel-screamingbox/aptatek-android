package com.aptatek.aptatek.view.chart;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.data.ChartVM;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.chart.adapter.ChartAdapter;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartActivity extends BaseActivity<ChartActivityView, ChartActivityPresenter> implements ChartActivityView {


    @Inject
    ChartActivityPresenter presenter;

    @Inject
    ChartAdapter chartAdapter;


    @BindView(R.id.scrollView)
    DiscreteScrollView bubbleScrollView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);

        List<ChartVM> chartVMList = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            chartVMList.add(new ChartVM(null));
        }

        chartAdapter.setItems(chartVMList);

        bubbleScrollView.setAdapter(chartAdapter);
        bubbleScrollView.setSlideOnFling(true);
        bubbleScrollView.setOverScrollEnabled(true);
        bubbleScrollView.setOffscreenItems(2);
        bubbleScrollView.setSlideOnFlingThreshold(700);
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @NonNull
    @Override
    public ChartActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_chart;
    }

}
