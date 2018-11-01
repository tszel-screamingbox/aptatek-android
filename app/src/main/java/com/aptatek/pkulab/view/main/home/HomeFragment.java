package com.aptatek.pkulab.view.main.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class HomeFragment extends BaseFragment implements HomeFragmentView, DiscreteScrollView.ScrollStateChangeListener {

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
        bubbleScrollView.setVisibility(GONE); //TODO: later, check if DB is empty or not

        recyclerViewDailyResults.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDailyResults.setAdapter(dailyResultsAdapter);
        recyclerViewDailyResults.addItemDecoration(dailyResultItemDecorator);
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

        if (chartAdapter.getItemCount() > 0) {
            presenter.loadData();
        }
    }


    @Override
    public boolean onBackPressed() {
        if (resultListContainer.getVisibility() == View.VISIBLE) {
            resultListContainer.setVisibility(View.GONE);
            return false;
        } else {
            return super.onBackPressed();
        }
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

    @OnClick(R.id.newTestButton)
    public void onNewTestButtonClicked() {
        presenter.startNewTest();
    }

    @OnClick({R.id.settingsButton, R.id.bigSettingsButton})
    public void onSettingsButtonClicked() {
        getBaseActivity().launchActivity(SettingsActivity.starter(getContext()), false, BaseActivity.Animation.FADE);
    }

    @OnClick(R.id.playIcon)
    public void onPlayIconClicked() {
        bigSettingsButton.setVisibility(GONE);
        buttonsGroup.setVisibility(VISIBLE);
        presenter.loadData();
        ((MainHostActivity) getBaseActivity()).enableSlidingPanel();
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
        getBaseActivity().launchActivity(TestActivity.createStarter(getContext()), false, BaseActivity.Animation.FADE);
    }
}
