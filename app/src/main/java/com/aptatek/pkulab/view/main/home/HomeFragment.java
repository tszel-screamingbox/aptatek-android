package com.aptatek.pkulab.view.main.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.view.connect.ConnectReaderActivity;
import com.aptatek.pkulab.view.dialog.AlertDialogFragment;
import com.aptatek.pkulab.view.main.MainHostActivity;
import com.aptatek.pkulab.view.main.home.adapter.chart.ChartAdapter;
import com.aptatek.pkulab.view.main.home.adapter.chart.ChartVM;
import com.aptatek.pkulab.view.main.home.adapter.daily.DailyResultAdapterItem;
import com.aptatek.pkulab.view.main.home.adapter.daily.DailyResultsAdapter;
import com.aptatek.pkulab.view.settings.basic.SettingsActivity;
import com.aptatek.pkulab.view.test.TestActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class HomeFragment extends BaseFragment implements HomeFragmentView, DiscreteScrollView.ScrollStateChangeListener {

    private static final String TAG_BATTER_DIALOG = "aptatek.main.home.battery.dialog";
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
        return panelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED;
    }

    public void closeResultsPanel() {
        panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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
        getBaseActivity().launchActivity(ConnectReaderActivity.starter(requireContext()), false, BaseActivity.Animation.FADE);
//        getBaseActivity().launchActivity(SettingsActivity.starter(requireContext()), false, BaseActivity.Animation.FADE);
    }

    @OnClick(R.id.playIcon)
    public void onPlayIconClicked() {
        presenter.startNewTest();
    }

    @OnClick(R.id.imgCloseResults)
    public void onCloseResultsClicked() {
        panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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
            if (chartVM.isZoomed() && chartVM.getNumberOfMeasures() > 1
                    && panelLayout.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED) {
                panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
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
    public void showLowBatteryDialog() {
        final AlertDialogModel model = AlertDialogModel.builder()
                .setTitle(getString(R.string.home_battery_alert_title))
                .setMessage(getString(R.string.home_battery_alert_content))
                .setPositiveButtonText(getString(R.string.alertdialog_button_ok))
                .setCancelable(false)
                .build();

        final AlertDialogFragment dialogFragment = AlertDialogFragment.create(model, null);
        dialogFragment.show(getBaseActivity().getSupportFragmentManager(), TAG_BATTER_DIALOG);
    }

    @Override
    public void navigateToTestScreen() {
        getBaseActivity().launchActivity(TestActivity.createStarter(requireContext()), false, BaseActivity.Animation.FADE);
    }

    @Override
    public void showNoResultsInLast6Months() {
        // TODO temporary solution until an official decision is made
        Toast.makeText(getActivity(), "No results in last 6 months. Take a test first ... ", Toast.LENGTH_SHORT).show();

        playIcon.setVisibility(VISIBLE);
        bigSettingsButton.setVisibility(VISIBLE);
        buttonsGroup.setVisibility(GONE);
        bubbleScrollView.setVisibility(GONE);
    }
}
