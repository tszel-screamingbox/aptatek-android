package com.aptatek.aptatek.view.weekly;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.weekly.swipe.CustomViewPager;
import com.aptatek.aptatek.view.weekly.swipe.SwipeAdapter;

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
        activityComponent.inject(this);
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
        emptyGroup.setVisibility(View.GONE);
        chartViewPager.disableSwipe(false);
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

    private void initAdapter() {
        final int pageSize = presenter.numberOfWeeks();
        final SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), pageSize);
        chartViewPager.setAdapter(swipeAdapter);
        chartViewPager.disableSwipe(true);
        presenter.showPage(pageSize - 1);
    }

    @OnPageChange(R.id.viewpager)
    public void onPageChanged(final int state) {
        presenter.subTitle(presenter.numberOfWeeks() - state - 1);
    }

    @Override
    public void onSubtitleChanged(final String subtitle) {
        dateTextView.setText(subtitle);
    }

    @Override
    public void onLastPageReached() {
        rightArrowImageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFirstPageReached() {
        leftArrowImageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoadNextPage(final int page) {
        chartViewPager.setCurrentItem(page);
    }
}
