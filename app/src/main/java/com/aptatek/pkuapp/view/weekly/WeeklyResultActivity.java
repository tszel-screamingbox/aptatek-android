package com.aptatek.pkuapp.view.weekly;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.injection.module.chart.ChartModule;
import com.aptatek.pkuapp.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkuapp.view.base.BaseActivity;
import com.aptatek.pkuapp.view.weekly.swipe.CustomViewPager;
import com.aptatek.pkuapp.view.weekly.swipe.SwipeAdapter;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

public class WeeklyResultActivity extends BaseActivity<WeeklyResultActivityView, WeeklyResultActivityPresenter> implements WeeklyResultActivityView {

    @Inject
    WeeklyResultActivityPresenter presenter;

    @BindView(R.id.emptyGroup)
    Group emptyGroup;

    @BindView(R.id.viewpager)
    CustomViewPager chartViewPager;

    @BindView(R.id.dateText)
    TextView dateTextView;

    @BindView(R.id.leftArrow)
    ImageView leftArrowImageView;

    @BindView(R.id.rightArrow)
    ImageView rightArrowImageView;

    @BindView(R.id.label)
    TextView tvUnit;

    @BindView(R.id.pdfExport)
    ImageView pdfExportButton;

    private SwipeAdapter swipeAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);
        ButterKnife.bind(this);

        // starts with "empty view"
        leftArrowImageView.setVisibility(View.INVISIBLE);
        rightArrowImageView.setVisibility(View.INVISIBLE);

        initAdapter();
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.plus(new RangeInfoModule(), new ChartModule())
                .inject(this);
    }

    @NonNull
    @Override
    public WeeklyResultActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return 0;
    }

    @OnClick(R.id.playIcon)
    public void onPlayButtonClicked() {
        pdfExportButton.setVisibility(View.VISIBLE);
        presenter.loadValidWeeks();
    }

    @OnClick(R.id.leftArrow)
    public void onLeftArrowClicked() {
        final int currentPage = chartViewPager.getCurrentItem();
        presenter.showPage(currentPage - 1);
    }

    @OnClick(R.id.rightArrow)
    public void onRightArrowClicked() {
        final int currentPage = chartViewPager.getCurrentItem();
        presenter.showPage(currentPage + 1);
    }

    @OnClick(R.id.pdfExport)
    public void onPdfExportClicked() {
        // TODO: pdf export
        Toast.makeText(this, "Pdf export", Toast.LENGTH_SHORT).show();
    }

    private void initAdapter() {
        swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), Collections.emptyList());
        chartViewPager.setAdapter(swipeAdapter);
        chartViewPager.disableSwipe(true);
    }

    @OnPageChange(R.id.viewpager)
    public void onPageChanged(final int state) {
        presenter.subTitle(presenter.getValidWeeks().size() - state - 1);
        presenter.updateArrows(state);
    }

    @Override
    public void onSubtitleChanged(final String subtitle) {
        dateTextView.setText(subtitle);
    }

    @Override
    public void onUpdateRightArrow(final boolean isVisible) {
        rightArrowImageView.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onUpdateLeftArrow(final boolean isVisible) {
        leftArrowImageView.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onLoadNextPage(final int page) {
        chartViewPager.setCurrentItem(page, true);
    }

    @Override
    public void displayUnitLabel(final String unitLabel) {
        tvUnit.setText(unitLabel);
    }

    @Override
    public void displayValidWeekList(final List<Integer> validWeeks) {
        emptyGroup.setVisibility(View.GONE);
        swipeAdapter.setData(validWeeks);
        chartViewPager.disableSwipe(false);
        chartViewPager.setCurrentItem(validWeeks.size() - 1);
    }
}
