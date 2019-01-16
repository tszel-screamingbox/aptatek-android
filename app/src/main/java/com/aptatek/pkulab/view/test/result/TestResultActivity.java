package com.aptatek.pkulab.view.test.result;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.rangeinfo.RangeInfoActivity;
import com.aptatek.pkulab.widget.BubbleTextView;
import com.aptatek.pkulab.widget.HeaderView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestResultActivity extends BaseActivity<TestResultView, TestResultPresenter> implements TestResultView {

    public static Intent starter(@NonNull final Context context) {
        return new Intent(context, TestResultActivity.class);
    }

    @Inject
    TestResultPresenter presenter;

    @BindView(R.id.header)
    HeaderView headerView;
    @BindView(R.id.test_result_bubble)
    BubbleTextView bubbleTextView;

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.plus(new TestModule(), new RangeInfoModule())
                .inject(this);
    }

    @Override
    public int getFrameLayoutId() {
        return 0;
    }

    @NonNull
    @Override
    public TestResultPresenter createPresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.initUi();
    }

    @Override
    public void render(@NonNull final TestResultState state) {
        final TextView titleTextView = headerView.getTitleTextView();
        final TextView subTitleTextView = headerView.getSubtitleTextView();
        if (state.isTitleVisible()) {
            titleTextView.setText(state.getTitle());
            titleTextView.setTextColor(state.getColor());
            titleTextView.setVisibility(View.VISIBLE);
        } else {
            titleTextView.setVisibility(View.GONE);
        }

        final String message = state.getMessage();
        subTitleTextView.setVisibility(TextUtils.isEmpty(message) ? View.INVISIBLE : View.VISIBLE);
        subTitleTextView.setText(state.getMessage());

        bubbleTextView.setConfiguration(BubbleTextView.BubbleTextConfiguration.builder()
                .setCircleColor(state.getColor())
                .setCircleWidth((int) getResources().getDimension(R.dimen.test_result_circle_width))
                .setFillColor(ContextCompat.getColor(this, R.color.applicationWhite))
                .setTextColor(state.getColor())
                .setPrimaryText(state.getFormattedPkuValue())
                .setSecondaryText(state.getTitle())
                .build());
    }

    @OnClick(R.id.test_result_done)
    public void onClickDone() {
        finish();
    }

    @OnClick(R.id.test_result_range_info)
    public void onClickResults() {
        startActivity(RangeInfoActivity.starter(this));
    }
}
