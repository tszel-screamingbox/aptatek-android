package com.aptatek.pkulab.view.test.result;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.rangeinfo.RangeInfoActivity;
import com.aptatek.pkulab.view.test.dispose.DisposeActivity;
import com.aptatek.pkulab.widget.BubbleTextView;
import com.aptatek.pkulab.widget.HeaderView;
import com.uxcam.UXCam;

import javax.inject.Inject;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestResultActivity extends BaseActivity<TestResultView, TestResultPresenter> implements TestResultView {

    @Arg
    String resultId;

    public static Intent starter(@NonNull final Context context, final String resultId) {
        return TestResultActivityStarter.getIntent(context, resultId)
                .addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
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

        ActivityStarter.fill(this, savedInstanceState);
        UXCam.occludeSensitiveView(bubbleTextView);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.initUi(resultId);
        presenter.resetTestScreen();
    }

    @Override
    public void render(@NonNull final TestResultState state) {
        final TextView titleTextView = headerView.getTitleTextView();
        final TextView subTitleTextView = headerView.getSubtitleTextView();

        titleTextView.setText(state.getTitle());
        titleTextView.setTextColor(state.getColor());
        subTitleTextView.setText(R.string.test_result_message);

        bubbleTextView.setConfiguration(BubbleTextView.BubbleTextConfiguration.builder()
                .setCircleColor(state.getColor())
                .setCircleWidth((int) getResources().getDimension(R.dimen.test_result_circle_width))
                .setFillColor(ContextCompat.getColor(this, R.color.applicationWhite))
                .setTextColor(state.getColor())
                .setPrimaryText(state.getFormattedPkuValue())
                .setSecondaryText(state.getPkuUnit())
                .build());
    }

    @OnClick(R.id.test_result_done)
    public void onClickDone() {
        launchActivity(new Intent(this, DisposeActivity.class));
        finish();
    }

    @OnClick(R.id.test_result_range_info)
    public void onClickResults() {
        launchActivity(RangeInfoActivity.starter(this));
    }
}
