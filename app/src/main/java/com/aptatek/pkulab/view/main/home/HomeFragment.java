package com.aptatek.pkulab.view.main.home;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.domain.model.ContinueTestResultType;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisions;
import com.aptatek.pkulab.view.dialog.AlertDialogFragment;
import com.aptatek.pkulab.view.main.MainHostActivity;
import com.aptatek.pkulab.view.main.continuetest.ContinueTestActivity;
import com.aptatek.pkulab.view.main.home.adapter.chart.ChartAdapter;
import com.aptatek.pkulab.view.main.home.adapter.chart.ChartVM;
import com.aptatek.pkulab.view.main.home.adapter.daily.DailyResultAdapterItem;
import com.aptatek.pkulab.view.main.home.adapter.daily.DailyResultsAdapter;
import com.aptatek.pkulab.view.settings.basic.SettingsActivity;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsActivity;
import com.aptatek.pkulab.view.test.TestActivity;
import com.aptatek.pkulab.view.test.result.TestResultActivity;
import com.aptatek.pkulab.widget.HeaderView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.aptatek.pkulab.view.base.BaseActivity.Animation.RIGHT_TO_LEFT;


public class HomeFragment extends BaseFragment implements HomeFragmentView, DiscreteScrollView.ScrollStateChangeListener {

    private static final String TAG_RANGE_DIALOG = "aptatek.main.home.range.dialog";
    private static final String TAG_UNFINISHED_DIALOG = "aptatek.main.home.test.unfinished.dialog";
    private static final String TAG_CONTINUE_TEST_DIALOG = "aptatek.main.home.test.continue.dialog";
    private static final String TAG_TEST_CANNOT_BE_FINISHED_DIALOG = "aptatek.main.home.test.continue.cannot.be.finished.dialog";
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
                panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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

    public boolean handleDispatchTouchEvent(final MotionEvent ev) {
        return panelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.DRAGGING
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
        mainHeaderView.setTitle(title);
        mainHeaderView.setSubtitle(subTitle);
    }

    @Override
    public void setMeasureList(final List<DailyResultAdapterItem> data) {
        dailyResultsAdapter.setData(data);
    }

    @Override
    public void showRangeDialog() {
        final AlertDialogModel model = AlertDialogModel.builder()
                .setTitle(getString(R.string.home_range_dialog_title))
                .setMessage(getString(R.string.home_range_dialog_message))
                .setPositiveButtonText(getString(R.string.home_range_dialog_set))
                .setNegativeButtonText(getString(R.string.home_range_dialog_later))
                .setCancelable(false)
                .build();

        final AlertDialogFragment dialogFragment = AlertDialogFragment.create(
                model,
                decision -> {
                    if (decision == AlertDialogDecisions.POSITIVE) {
                        final Intent intent = RangeSettingsActivity.starter(getContext());
                        getBaseActivity().launchActivity(intent, false, RIGHT_TO_LEFT);
                    }
                });
        dialogFragment.show(getBaseActivity().getSupportFragmentManager(), TAG_RANGE_DIALOG);
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
    public void unfinishedTestDetected() {
        final AlertDialogModel model = AlertDialogModel.builder()
                .setTitle(getString(R.string.home_test_unfinished_title))
                .setMessage(getString(R.string.home_test_unfinished_message))
                .setPositiveButtonText(getString(R.string.alertdialog_button_yes))
                .setNegativeButtonText(getString(R.string.alertdialog_button_no))
                .setCancelable(false)
                .build();

        final AlertDialogFragment dialogFragment = AlertDialogFragment.create(
                model,
                decision -> {
                    if (decision == AlertDialogDecisions.POSITIVE) {
                        getBaseActivity().launchActivityForResult(ContinueTestActivity.starter(getActivity()), BaseActivity.Animation.RIGHT_TO_LEFT, ContinueTestActivity.CONTINUE_TEST_ACTIVITY_REQUEST_CODE);
                    } else {
                        showContinueTestDialog();
                    }
                });
        dialogFragment.show(getBaseActivity().getSupportFragmentManager(), TAG_UNFINISHED_DIALOG);
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

    private void showContinueTestDialog() {
        final AlertDialogModel model = AlertDialogModel.builder()
                .setTitle(getString(R.string.home_test_continue_dialog_title))
                .setMessage(getString(R.string.home_test_continue_dialog_message))
                .setPositiveButtonText(getString(R.string.home_test_continue_dialog_next))
                .setCancelable(false)
                .build();

        final AlertDialogFragment dialogFragment = AlertDialogFragment.create(
                model,
                decision -> {
                    if (decision == AlertDialogDecisions.POSITIVE) {
                        getBaseActivity().launchActivity(TestActivity.createStarter(requireContext()), false, BaseActivity.Animation.FADE);
                    }
                });
        dialogFragment.show(getBaseActivity().getSupportFragmentManager(), TAG_CONTINUE_TEST_DIALOG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ContinueTestActivity.CONTINUE_TEST_ACTIVITY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK && data != null && data.hasExtra(Constants.CONTINUE_TEST_RESULT_TYPE_KEY)) {

            final ContinueTestResultType resultType = (ContinueTestResultType) data.getSerializableExtra(Constants.CONTINUE_TEST_RESULT_TYPE_KEY);

            if (resultType == ContinueTestResultType.FINISHED_WITH_CORRECT_RESULT) {
                getBaseActivity().launchActivity(TestResultActivity.starter(requireContext()), false, BaseActivity.Animation.FADE);
            } else if (resultType == ContinueTestResultType.FINISHED_WITH_WRONG_RESULT) {
                final AlertDialogModel model = AlertDialogModel.builder()
                        .setTitle(getString(R.string.home_test_continue_dialog_cannot_be_finished_title))
                        .setMessage(getString(R.string.home_test_continue_dialog_cannot_be_finished_message))
                        .setPositiveButtonText(getString(R.string.home_test_continue_dialog_next))
                        .setCancelable(false)
                        .build();

                final AlertDialogFragment dialogFragment = AlertDialogFragment.create(
                        model,
                        decision -> {
                        });
                dialogFragment.show(getBaseActivity().getSupportFragmentManager(), TAG_TEST_CANNOT_BE_FINISHED_DIALOG);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
