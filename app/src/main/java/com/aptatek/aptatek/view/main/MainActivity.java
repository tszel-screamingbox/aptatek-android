package com.aptatek.aptatek.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.data.ChartVM;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.main.adapter.ChartAdapter;
import com.aptatek.aptatek.view.main.adapter.ChartAdapterViewHolder;
import com.aptatek.aptatek.view.settings.basic.SettingsActivity;
import com.aptatek.aptatek.view.test.TestActivity;
import com.aptatek.aptatek.view.toggle.ToggleActivity;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainActivityView, MainActivityPresenter> implements MainActivityView, DiscreteScrollView.ScrollStateChangeListener {

    @Inject
    MainActivityPresenter presenter;

    @Inject
    ChartAdapter chartAdapter;


    @BindView(R.id.scrollView)
    DiscreteScrollView bubbleScrollView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    public MainActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_main;
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

    @OnClick(R.id.resultButton)
    public void onToggleButtonClicked() {
        final Intent intent = new Intent(this, ToggleActivity.class);
        launchActivity(intent, false, Animation.FADE);
    }

    @OnClick(R.id.newTestButton)
    public void onNewTestButtonClicked() {
        launchActivity(TestActivity.createStarter(this), false, Animation.FADE);
    }

    @OnClick(R.id.settingsButton)
    public void onSettingsButtonClicked() {
        launchActivity(SettingsActivity.starter(this), false, Animation.FADE);
    }
}
