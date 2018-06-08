package com.aptatek.aptatek.view.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.base.BaseFragment;
import com.aptatek.aptatek.view.test.base.TestFragmentBaseView;
import com.aptatek.aptatek.view.test.incubation.IncubationFragment;
import com.aptatek.aptatek.view.test.takesample.TakeSampleFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends BaseActivity<TestActivityView, TestActivityPresenter>
    implements TestActivityView {

    public static Intent createStarter(@NonNull final Context context) {
        return new Intent(context, TestActivity.class);
    }

    @Inject
    TestActivityPresenter testActivityPresenter;

    @BindView(R.id.testCancelCircleButton)
    View cancelCircle;
    @BindView(R.id.testCancelButton)
    View cancelButton;
    @BindView(R.id.testNavigationButton)
    Button navigationButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        switchToFragment(new TakeSampleFragment());
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getFrameLayoutId() {
        return R.id.testContent;
    }

    @NonNull
    @Override
    public TestActivityPresenter createPresenter() {
        return testActivityPresenter;
    }

    @OnClick({R.id.testCancelCircleButton, R.id.testCancelButton})
    void onCancelClicked() {
        navigateBack();
    }

    @OnClick(R.id.testNavigationButton)
    void onNavigationClicked() {
        navigateForward();
    }

    @Override
    public void setCircleCancelVisible(final boolean visible) {
        cancelCircle.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setCancelBigVisible(final boolean visible) {
        cancelButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setNavigationButtonVisible(final boolean visible) {
        navigationButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setNavigationButtonText(@NonNull final String buttonText) {
        navigationButton.setText(buttonText);
    }

    @Override
    public void navigateBack() {
        final BaseFragment activeBaseFragment = getActiveBaseFragment();
        if (activeBaseFragment instanceof TestFragmentBaseView) {
            ((TestFragmentBaseView) activeBaseFragment).onNavigateBackPressed();
        }

        onBackPressed();
    }

    @Override
    public void navigateForward() {
        final BaseFragment activeBaseFragment = getActiveBaseFragment();
        if (activeBaseFragment instanceof TestFragmentBaseView) {
            ((TestFragmentBaseView) activeBaseFragment).onNavigateForwardPressed();
        }

        switchToFragment(new IncubationFragment());
    }
}
