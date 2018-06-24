package com.aptatek.aptatek.view.chart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.data.ChartVM;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.chart.adapter.ChartAdapter;
import com.aptatek.aptatek.view.chart.adapter.ChartAdapterViewHolder;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartActivity extends BaseActivity<ChartActivityView, ChartActivityPresenter> implements ChartActivityView, DiscreteScrollView.ScrollStateChangeListener {


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

        bubbleScrollView.setAdapter(chartAdapter);
        bubbleScrollView.setSlideOnFling(true);
        bubbleScrollView.setOverScrollEnabled(true);
        bubbleScrollView.setSlideOnFlingThreshold(500);
        bubbleScrollView.setItemTransitionTimeMillis(200);
        bubbleScrollView.addScrollStateChangeListener(this);
        bubbleScrollView.addOnItemChangedListener((viewHolder, adapterPosition) -> {
            final ChartAdapterViewHolder holder = (ChartAdapterViewHolder) viewHolder;
            if (holder != null) {
                holder.showDetails();
            }
        });

        chartAdapter.setItems(chartVMList);
        chartAdapter.setOnItemClickListener(chartVM -> {
            final int selectedIndex = chartAdapter.getItemPosition(chartVM);
            bubbleScrollView.smoothScrollToPosition(selectedIndex);
        });
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

    @Override
    public void onScrollStart(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {
        final ChartAdapterViewHolder viewHolder = (ChartAdapterViewHolder) currentItemHolder;
        viewHolder.hideDetails();
    }

    @Override
    public void onScrollEnd(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {

    }
}
