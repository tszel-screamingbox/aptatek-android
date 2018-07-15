package com.aptatek.aptatek.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.main.adapter.ChartAdapter;
import com.aptatek.aptatek.view.main.adapter.ChartAdapterViewHolder;
import com.aptatek.aptatek.view.settings.basic.SettingsActivity;
import com.aptatek.aptatek.view.test.TestActivity;
import com.aptatek.aptatek.view.toggle.ToggleActivity;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

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

    @BindView(R.id.titleText)
    TextView titleTextView;

    @BindView(R.id.subTitleText)
    TextView subTitleTextView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initAdapter();
        bubbleScrollView.setVisibility(View.GONE); //TODO: later, check if DB is empty or not
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
    public void onScrollStart(@NonNull final RecyclerView.ViewHolder currentItemHolder, final int adapterPosition) {
        final ChartAdapterViewHolder viewHolder = (ChartAdapterViewHolder) currentItemHolder;
        viewHolder.hideDetails();
    }

    @Override
    public void onScrollEnd(@NonNull final RecyclerView.ViewHolder currentItemHolder, final int adapterPosition) {

    }

    @Override
    public void onScroll(final float scrollPosition, final int currentPosition, final int newPosition, @Nullable final RecyclerView.ViewHolder currentHolder, @Nullable final RecyclerView.ViewHolder newCurrent) {

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

    @OnClick(R.id.playIcon)
    public void onPlayIconClicked(final ImageView icon) {
        bubbleScrollView.setVisibility(View.VISIBLE);
        icon.setVisibility(View.GONE);
        chartAdapter.setItems(presenter.fakeData());
        bubbleScrollView.scrollToPosition(chartAdapter.getItemCount());
    }

    private void initAdapter() {
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
            presenter.itemChanged(chartAdapter.getItem(adapterPosition));
        });
        chartAdapter.setOnItemClickListener(chartVM -> {
            final int selectedIndex = chartAdapter.getItemPosition(chartVM);
            bubbleScrollView.smoothScrollToPosition(selectedIndex);
            //TODO: show Details
        });
    }

    @Override
    public void updateTitles(final String title, final String subTitle) {
        titleTextView.setText(title);
        subTitleTextView.setText(subTitle);
    }
}
