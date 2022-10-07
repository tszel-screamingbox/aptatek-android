package com.aptatek.pkulab.view.test.result;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.view.View;
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
    @Arg
    boolean fromNotification;

    public static Intent starter(@NonNull final Context context, final String resultId, final boolean fromNotification) {
        return TestResultActivityStarter.getIntent(context, resultId, fromNotification)
                .addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
    }

    @Inject
    TestResultPresenter presenter;

    @BindView(R.id.header)
    HeaderView headerView;
    @BindView(R.id.test_result_bubble)
    BubbleTextView bubbleTextView;
    @BindView(R.id.test_result_bubble_fake)
    View bubbleFake;

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
        ButterKnife.bind(this);
        UXCam.occludeSensitiveView(bubbleTextView);

        if (fromNotification) {
            presenter.logOpenFromNotification();
        }
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

//        titleTextView.setText(state.getTitle());
//        titleTextView.setTextColor(state.getColor());
//        subTitleTextView.setText(R.string.test_result_message);

        titleTextView.setText(state.isValid() ? R.string.test_result_success_title : R.string.test_result_invalid_title);
        titleTextView.setTextColor(ContextCompat.getColor(this, state.isValid() ? R.color.applicationGreen : R.color.applicationRed));
        subTitleTextView.setText(state.isValid() ? R.string.test_result_success_message : R.string.test_result_invalid_message);

        bubbleFake.setVisibility(state.isValid() ? View.VISIBLE : View.GONE);

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
        presenter.logTestDoneAnd(() -> {
            launchActivity(new Intent(TestResultActivity.this, DisposeActivity.class));
            finish();
        });
    }

    @OnClick(R.id.test_result_range_info)
    public void onClickResults() {
        launchActivity(RangeInfoActivity.starter(this));
    }
}
