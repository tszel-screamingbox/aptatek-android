package com.aptatek.aptatek.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.main.adapter.ChartAdapter;
import com.aptatek.aptatek.view.main.adapter.ChartVM;
import com.aptatek.aptatek.view.main.adapter.DailyResultAdapterItem;
import com.aptatek.aptatek.view.main.adapter.DailyResultsAdapter;
import com.aptatek.aptatek.view.settings.basic.SettingsActivity;
import com.aptatek.aptatek.view.test.TestActivity;
import com.aptatek.aptatek.view.weekly.WeeklyResultActivity;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainActivityView, MainActivityPresenter> implements MainActivityView, DiscreteScrollView.ScrollStateChangeListener {

    private static final int TRESHOLD = 500;
    private static final int TRANSITION_TIME = 200;

    @Inject
    MainActivityPresenter presenter;

    @Inject
    DailyResultItemDecorator dailyResultItemDecorator;

    @Inject
    ChartAdapter chartAdapter;

    @Inject
    DailyResultsAdapter dailyResultsAdapter;

    @BindView(R.id.scrollView)
    DiscreteScrollView bubbleScrollView;

    @BindView(R.id.titleText)
    TextView titleTextView;

    @BindView(R.id.subTitleText)
    TextView subTitleTextView;

    @BindView(R.id.resultListContainer)
    ConstraintLayout resultListContainer;

    @BindView(R.id.recyclerViewDailyResults)
    RecyclerView recyclerViewDailyResults;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initAdapter();
        bubbleScrollView.setVisibility(View.GONE); //TODO: later, check if DB is empty or not

        recyclerViewDailyResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDailyResults.setAdapter(dailyResultsAdapter);
        recyclerViewDailyResults.addItemDecoration(dailyResultItemDecorator);
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
        presenter.itemZoomOut(chartAdapter.getItem(adapterPosition));
    }

    @Override
    public void onScrollEnd(@NonNull final RecyclerView.ViewHolder currentItemHolder, final int adapterPosition) {
        presenter.itemZoomIn(chartAdapter.getItem(adapterPosition));
    }

    @Override
    public void onScroll(final float scrollPosition, final int currentPosition, final int newPosition, @Nullable final RecyclerView.ViewHolder currentHolder, @Nullable final RecyclerView.ViewHolder newCurrent) {

    }

    @OnClick(R.id.resultButton)
    public void onToggleButtonClicked() {
        final Intent intent = new Intent(this, WeeklyResultActivity.class);
        launchActivity(intent, false, Animation.RIGHT_TO_LEFT);
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

    @OnClick(R.id.imgCloseResults)
    public void onCloseResultsClicked() {
        resultListContainer.setVisibility(View.GONE);
    }

    private void initAdapter() {
        bubbleScrollView.setAdapter(chartAdapter);
        bubbleScrollView.setSlideOnFling(true);
        bubbleScrollView.setOverScrollEnabled(true);
        bubbleScrollView.setSlideOnFlingThreshold(TRESHOLD);
        bubbleScrollView.setItemTransitionTimeMillis(TRANSITION_TIME);
        bubbleScrollView.addScrollStateChangeListener(this);
        bubbleScrollView.addOnItemChangedListener((viewHolder, adapterPosition) -> {
//            presenter.itemZoomIn(chartAdapter.getItem(adapterPosition));
        });
        chartAdapter.setOnItemClickListener(chartVM -> {
            final int selectedIndex = chartAdapter.getItemPosition(chartVM);
            bubbleScrollView.smoothScrollToPosition(selectedIndex);
            if (chartVM.isZoomed() && chartVM.getNumberOfMeasures() > 1 && resultListContainer.getVisibility() == View.GONE) {
                resultListContainer.setVisibility(View.VISIBLE);
                presenter.measureListToAdapterList(chartVM.getMeasures());
            }
        });
    }

    @Override
    public void changeItemZoomState(final ChartVM oldItem, final ChartVM newItem) {
        chartAdapter.updateItem(oldItem, newItem);
    }

    @Override
    public void updateTitles(final String title, final String subTitle) {
        titleTextView.setText(title);
        subTitleTextView.setText(subTitle);
    }

    @Override
    public void setMeasureList(final List<DailyResultAdapterItem> data) {
        dailyResultsAdapter.setData(data);
    }
}
