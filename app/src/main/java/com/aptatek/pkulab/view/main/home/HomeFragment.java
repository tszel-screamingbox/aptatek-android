package com.aptatek.pkulab.view.main.home;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aptatek.pkulab.BuildConfig;
import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation.UnfinishedTest;
import com.aptatek.pkulab.domain.model.ContinueTestResultType;
import com.aptatek.pkulab.domain.model.TestContinueDialogModel;
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
import com.aptatek.pkulab.view.main.weekly.csv.Attachment;
import com.aptatek.pkulab.view.settings.basic.SettingsActivity;
import com.aptatek.pkulab.view.test.TestActivity;
import com.aptatek.pkulab.view.test.dispose.DisposeActivity;
import com.aptatek.pkulab.view.test.result.TestResultActivity;
import com.aptatek.pkulab.widget.HeaderView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;


public class HomeFragment extends BaseFragment implements HomeFragmentView, DiscreteScrollView.ScrollStateChangeListener {

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

    @Inject
    IAnalyticsManager analyticsManager;

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

    @BindView(R.id.exportButton)
    View exportButton;

    private Runnable runOnAttach = null;

    @Override
    protected List<View> sensitiveViewList() {
        final List<View> list = new ArrayList<>();
        list.add(unitTextView);
        list.add(bubbleScrollView);
        list.add(mainHeaderView.getSubtitleTextView());
        return list;
    }

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

        exportButton.setVisibility(Constants.showResults ? GONE : VISIBLE);
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.plus(new TestModule(), new RangeInfoModule(), new ChartModule())
                .inject(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (runOnAttach != null) {
            runOnAttach.run();
            runOnAttach = null;
        }
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
        bubbleScrollView.scrollToPosition(chartAdapter.getItemCount() - 1);
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
            if (selectedIndex >= 0 && selectedIndex < chartAdapter.getItemCount()) {
                bubbleScrollView.smoothScrollToPosition(selectedIndex);
            }
            if (chartVM.isZoomed() && chartVM.getNumberOfMeasures() > 1 && !isResultShown()) {
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
    public void showLastResult(String resultId) {
        getBaseActivity().launchActivity(TestResultActivity.starter(requireContext(), resultId, false), false, BaseActivity.Animation.FADE);
    }

    @Override
    public void navigateToTestScreen() {
        getBaseActivity().launchActivity(TestActivity.createStarter(requireContext()), false, BaseActivity.Animation.FADE);
    }

    @Override
    public void unfinishedTestDetected() {
        final AlertDialogFragment dialogFragment = AlertDialogFragment.create(
                TestContinueDialogModel.unfinishedTestDialogModelCreate(requireContext()),
                decision -> {
                    if (decision == AlertDialogDecisions.POSITIVE) {
                        getBaseActivity().launchActivityForResult(ContinueTestActivity.starter(getActivity()), BaseActivity.Animation.RIGHT_TO_LEFT, ContinueTestActivity.CONTINUE_TEST_ACTIVITY_REQUEST_CODE);
                    } else {
                        showContinueTestDialog();
                    }
                });
        dialogFragment.setCancelable(false);
        dialogFragment.show(getBaseActivity().getSupportFragmentManager(), TAG_UNFINISHED_DIALOG);
    }

    public void showNoResults() {
        updateTitles(getString(R.string.main_title_noresult), getString(R.string.main_title_noresult_hint));

        playIcon.setVisibility(VISIBLE);
        bigSettingsButton.setVisibility(VISIBLE);
        buttonsGroup.setVisibility(GONE);
        bubbleScrollView.setVisibility(GONE);
    }

    private void showContinueTestDialog() {
        final AlertDialogFragment dialogFragment = AlertDialogFragment.create(
                TestContinueDialogModel.continueTestDialogModelCreate(requireContext()),
                decision -> {
                    if (decision == AlertDialogDecisions.POSITIVE) {
                        analyticsManager.logEvent(new UnfinishedTest("risk_unfinished_test_continue"));
                        getBaseActivity().launchActivity(TestActivity.createStarter(requireContext()), false, BaseActivity.Animation.FADE);
                    } else {
                        analyticsManager.logEvent(new UnfinishedTest("risk_unfinished_test_failed"));
                    }
                });
        dialogFragment.setCancelable(false);
        dialogFragment.show(getBaseActivity().getSupportFragmentManager(), TAG_CONTINUE_TEST_DIALOG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ContinueTestActivity.CONTINUE_TEST_ACTIVITY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK && data != null && data.hasExtra(Constants.CONTINUE_TEST_RESULT_TYPE_KEY)) {

            final ContinueTestResultType resultType = (ContinueTestResultType) data.getSerializableExtra(Constants.CONTINUE_TEST_RESULT_TYPE_KEY);

            if (resultType == ContinueTestResultType.FINISHED_WITH_CORRECT_RESULT) {
                presenter.showLastResult();
            } else if (resultType == ContinueTestResultType.FINISHED_WITH_WRONG_RESULT) {
                if (getActivity() == null) {
                    runOnAttach = () -> {
                        final AlertDialogFragment dialogFragment = AlertDialogFragment.create(
                                TestContinueDialogModel.incorrectResultDialogModelCreate(getActivity()),
                                decision -> getBaseActivity().launchActivity(new Intent(getActivity(), DisposeActivity.class)));
                        dialogFragment.setCancelable(false);
                        dialogFragment.show(getBaseActivity().getSupportFragmentManager(), TAG_TEST_CANNOT_BE_FINISHED_DIALOG);
                    };
                } else {
                    final AlertDialogFragment dialogFragment = AlertDialogFragment.create(
                            TestContinueDialogModel.incorrectResultDialogModelCreate(getActivity()),
                            decision -> getBaseActivity().launchActivity(new Intent(getActivity(), DisposeActivity.class)));
                    dialogFragment.setCancelable(false);
                    dialogFragment.show(getBaseActivity().getSupportFragmentManager(), TAG_TEST_CANNOT_BE_FINISHED_DIALOG);
                }
            } else if (resultType == ContinueTestResultType.FINISHED_WITH_TEST_RUNNING) {
                if (isVisible() || getActivity() != null) {
                    getBaseActivity().launchActivity(TestActivity.createStarter(getBaseActivity()), false, BaseActivity.Animation.FADE);
                } else {
                    runOnAttach = () -> {
                        Timber.d("--- finished with test running runOnAttach");
                        getBaseActivity().launchActivity(TestActivity.createStarter(getBaseActivity()), false, BaseActivity.Animation.FADE);
                    };
                }
            }
        }
    }

    @OnClick(R.id.exportButton)
    public void onExportClicked() {
        presenter.getCsvData();
    }

    @Override
    public void onCsvReady(Attachment attachment) {
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                attachment.getCsvFile()));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.csv_export_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, attachment.getBody());
        startActivity(Intent.createChooser(emailIntent, ""));
    }
}
