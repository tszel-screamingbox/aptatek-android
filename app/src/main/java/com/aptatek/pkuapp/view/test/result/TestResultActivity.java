package com.aptatek.pkuapp.view.test.result;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.injection.module.test.TestModule;
import com.aptatek.pkuapp.view.base.BaseActivity;
import com.aptatek.pkuapp.view.rangeinfo.RangeInfoActivity;

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

    @BindView(R.id.test_result_title)
    TextView tvTitle;
    @BindView(R.id.test_result_message)
    TextView tvMessage;
    @BindView(R.id.test_result_bubble_value)
    TextView tvBubbleValue;
//    @BindView(R.id.test_result_bubble_message)
//    TextView tvBubbleMessage;
    @BindView(R.id.test_result_bubble)
    ConstraintLayout clBubble;

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.plus(new TestModule())
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

        presenter.initUi();
    }

    @Override
    public void render(@NonNull final TestResultState state) {
        tvTitle.setText(state.getTitle());
        tvTitle.setTextColor(state.getColor());

        tvMessage.setVisibility(TextUtils.isEmpty(state.getMessage()) ? View.INVISIBLE : View.VISIBLE);
        tvMessage.setText(state.getMessage());

        tvBubbleValue.setText(state.getFormattedPkuValue());
        tvBubbleValue.setTextColor(state.getColor());

//        tvBubbleMessage.setText(state.getPkuLevelText());
//        tvBubbleMessage.setTextColor(state.getColor());
//        tvBubbleMessage.setTextSize(calculateTextSize(tvBubbleMessage, state.getPkuLevelText()));

        final Drawable background = clBubble.getBackground();
        if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setStroke(
                    getResources().getDimensionPixelSize(R.dimen.graph_bubble_stroke_big),
                    state.getColor());
        }
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
