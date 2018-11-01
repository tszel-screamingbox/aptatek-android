package com.aptatek.pkulab.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.main.adapter.chart.ChartAdapter;
import com.aptatek.pkulab.view.main.adapter.chart.ChartVM;
import com.aptatek.pkulab.view.main.adapter.daily.DailyResultAdapterItem;
import com.aptatek.pkulab.view.main.adapter.daily.DailyResultsAdapter;
import com.aptatek.pkulab.view.settings.basic.SettingsActivity;
import com.aptatek.pkulab.view.test.TestActivity;
import com.aptatek.pkulab.view.weekly.WeeklyResultActivity;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends BaseActivity<MainActivityView, MainActivityPresenter> implements MainActivityView, DiscreteScrollView.ScrollStateChangeListener {

    private static final int THRESHOLD = 500;
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

    @BindView(R.id.playIcon)
    ImageView playIcon;

    @BindView(R.id.buttonGroup)
    Group buttonsGroup;

    @BindView(R.id.bigSettingsButton)
    Button bigSettingsButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initAdapter();
        bubbleScrollView.setVisibility(GONE); //TODO: later, check if DB is empty or not

        recyclerViewDailyResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDailyResults.setAdapter(dailyResultsAdapter);
        recyclerViewDailyResults.addItemDecoration(dailyResultItemDecorator);
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.checkRunningTest();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (chartAdapter.getItemCount() > 0) {
            presenter.loadData();
        }
    }

    @Override
    public void onBackPressed() {
        if (resultListContainer.getVisibility() == VISIBLE) {
            resultListContainer.setVisibility(GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.plus(new TestModule(), new RangeInfoModule(), new ChartModule())
                .inject(this);
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

    @Override
    public void displayData(final List<ChartVM> data) {
        bubbleScrollView.setVisibility(VISIBLE);
        playIcon.setVisibility(GONE);
        chartAdapter.setItems(data);
        bubbleScrollView.scrollToPosition(chartAdapter.getItemCount());
    }

    @OnClick(R.id.weeklyButton)
    public void onToggleButtonClicked() {
        final Intent intent = new Intent(this, WeeklyResultActivity.class);
        launchActivity(intent, false, Animation.BOTTOM_TO_TOP);
    }

    @OnClick(R.id.newTestButton)
    public void onNewTestButtonClicked() {
        presenter.startNewTest();
    }

    @OnClick({R.id.settingsButton, R.id.bigSettingsButton})
    public void onSettingsButtonClicked() {
        launchActivity(SettingsActivity.starter(this), false, Animation.FADE);
    }

    @OnClick(R.id.playIcon)
    public void onPlayIconClicked() {
        bigSettingsButton.setVisibility(GONE);
        buttonsGroup.setVisibility(VISIBLE);
        presenter.loadData();
    }

    @OnClick(R.id.imgCloseResults)
    public void onCloseResultsClicked() {
        resultListContainer.setVisibility(GONE);
    }

    private void initAdapter() {
        bubbleScrollView.setAdapter(chartAdapter);
        bubbleScrollView.setOverScrollEnabled(true);
        bubbleScrollView.setSlideOnFlingThreshold(THRESHOLD);
        bubbleScrollView.setItemTransitionTimeMillis(TRANSITION_TIME);
        bubbleScrollView.addScrollStateChangeListener(this);
        chartAdapter.setOnItemClickListener(chartVM -> {
            final int selectedIndex = chartAdapter.getItemPosition(chartVM);
            bubbleScrollView.smoothScrollToPosition(selectedIndex);
            if (chartVM.isZoomed() && chartVM.getNumberOfMeasures() > 1 && resultListContainer.getVisibility() == GONE) {
                resultListContainer.setVisibility(VISIBLE);
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

    @Override
    public void navigateToTestScreen() {
        launchActivity(TestActivity.createStarter(this), false, Animation.FADE);
    }
}
