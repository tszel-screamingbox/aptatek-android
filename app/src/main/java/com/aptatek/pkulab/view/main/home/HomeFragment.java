package com.aptatek.pkulab.view.main.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.view.main.MainHostActivity;
import com.aptatek.pkulab.view.main.home.adapter.chart.ChartAdapter;
import com.aptatek.pkulab.view.main.home.adapter.chart.ChartVM;
import com.aptatek.pkulab.view.main.home.adapter.daily.DailyResultAdapterItem;
import com.aptatek.pkulab.view.main.home.adapter.daily.DailyResultsAdapter;
import com.aptatek.pkulab.view.settings.basic.SettingsActivity;
import com.aptatek.pkulab.view.test.TestActivity;
import com.aptatek.pkulab.widget.HeaderView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class HomeFragment extends BaseFragment implements HomeFragmentView, DiscreteScrollView.ScrollStateChangeListener {

    private static final String TAG_RANGE_DIALOG = "aptatek.main.home.range.dialog";
    private static final int THRESHOLD = 500;
    private static final int TRANSITION_TIME = 200;

    @Inject
    HomeFragmentPresenter presenter;

    @Inject
    DailyResultItemDecorator dailyResultItemDecorator;

    @Inject
    ChartAdapter chartAdapter;

    @Inject
    DailyResultsAdapter dailyResultsAdapter;

    @BindView(R.id.scrollView)
    DiscreteScrollView bubbleScrollView;

    @BindView(R.id.header)
    HeaderView mainHeaderView;

    @BindView(R.id.recyclerViewDailyResults)
    RecyclerView recyclerViewDailyResults;

    @BindView(R.id.playIcon)
    ImageView playIcon;

    @BindView(R.id.buttonGroup)
    Group buttonsGroup;

    @BindView(R.id.bigSettingsButton)
    Button bigSettingsButton;

    @BindView(R.id.panelLayout)
    SlidingUpPanelLayout panelLayout;

    @BindView(R.id.unitText)
    TextView unitTextView;

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initObjects(final View view) {
        initAdapter();

        recyclerViewDailyResults.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDailyResults.setAdapter(dailyResultsAdapter);
        recyclerViewDailyResults.addItemDecoration(dailyResultItemDecorator);

        bubbleScrollView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
                setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        presenter.initView();
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.plus(new TestModule(), new RangeInfoModule(), new ChartModule())
                .inject(this);
    }

    @NonNull
    @Override
    public HomeFragmentPresenter createPresenter() {
        return presenter;
    }


    @Override
    public void onStart() {
        super.onStart();

        presenter.checkRunningTest();
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.loadData();
    }

    public boolean isResultShown() {
        return panelLayout != null && panelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED;
    }

    public void closeResultsPanel() {
        setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    public boolean handleDispatchTouchEvent(final MotionEvent ev) {
        return panelLayout != null && panelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.DRAGGING
                && ev.getAction() == MotionEvent.ACTION_DOWN;
    }

    @OnClick(R.id.weeklyButton)
    public void onWeeklyResultsButtonClicked() {
        ((MainHostActivity) getBaseActivity()).showWeeklyChartPanel();
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
        ((MainHostActivity) getBaseActivity()).enableSlidingPanel();
        bubbleScrollView.setVisibility(VISIBLE);
        buttonsGroup.setVisibility(VISIBLE);
        unitTextView.setVisibility(VISIBLE);
        playIcon.setVisibility(GONE);
        bigSettingsButton.setVisibility(GONE);
        chartAdapter.setItems(data);
        bubbleScrollView.scrollToPosition(chartAdapter.getItemCount());
    }

    @OnClick(R.id.newTestButton)
    public void onNewTestButtonClicked() {
        presenter.startNewTest();
    }

    @OnClick({R.id.settingsButton, R.id.bigSettingsButton})
    public void onSettingsButtonClicked() {
        getBaseActivity().launchActivity(SettingsActivity.starter(requireContext()), false, BaseActivity.Animation.FADE);
    }

    @OnClick(R.id.playIcon)
    public void onPlayIconClicked() {
        presenter.startNewTest();
    }

    @OnClick(R.id.imgCloseResults)
    public void onCloseResultsClicked() {
        setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    private void setPanelState(final SlidingUpPanelLayout.PanelState panelState) {
        if (panelLayout != null) {
            panelLayout.setPanelState(panelState);
        }
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
            if (chartVM.isZoomed() && chartVM.getNumberOfMeasures() > 1 && isResultShown()) {
                setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
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
        mainHeaderView.setTitle(title);
        mainHeaderView.setSubtitle(subTitle);
    }

    @Override
    public void setMeasureList(final List<DailyResultAdapterItem> data) {
        dailyResultsAdapter.setData(data);
    }

    @Override
    public void updateUnitText(final String text) {
        unitTextView.setText(text);
    }

    @Override
    public void navigateToTestScreen() {
        getBaseActivity().launchActivity(TestActivity.createStarter(requireContext()), false, BaseActivity.Animation.FADE);
    }

    @Override
    public void showNoResults() {
        updateTitles(getString(R.string.main_title_noresult), getString(R.string.main_title_noresult_hint));

        playIcon.setVisibility(VISIBLE);
        bigSettingsButton.setVisibility(VISIBLE);
        buttonsGroup.setVisibility(GONE);
        bubbleScrollView.setVisibility(GONE);
    }
}
