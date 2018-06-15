package com.aptatek.aptatek.view.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.injection.module.test.TestModule;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.base.BaseFragment;
import com.aptatek.aptatek.view.test.base.TestFragmentBaseView;
import com.aptatek.aptatek.view.test.canceltest.CancelTestFragment;
import com.aptatek.aptatek.view.test.incubation.IncubationFragment;
import com.aptatek.aptatek.view.test.tutorial.attachcube.AttachCubeFragment;
import com.aptatek.aptatek.view.test.tutorial.insertcasette.InsertCasetteFragment;
import com.aptatek.aptatek.view.test.takesample.TakeSampleFragment;
import com.aptatek.aptatek.view.test.tutorial.insertsample.InsertSampleFragment;

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.showProperScreen();
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.plus(new TestModule())
                .inject(this);
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
    public void showScreen(@NonNull final TestScreens screen) {
        final BaseFragment fragment;
        final boolean clearStack;
        final boolean persistCurrent;

        switch (screen) {
            case CANCEL: {
                fragment = new CancelTestFragment();
                clearStack = false;
                persistCurrent = false;
                break;
            }
            case INCUBATION: {
                fragment = new IncubationFragment();
                clearStack = false;
                persistCurrent = false;
                break;
            }
            case INSERT_CASSETTE: {
                fragment = new InsertCasetteFragment();
                clearStack = false;
                persistCurrent = true;
                break;
            }
            case ATTACH_CUBE: {
                fragment = new AttachCubeFragment();
                clearStack = false;
                persistCurrent = true;
                break;
            }
            case INSERT_SAMPLE: {
                fragment = new InsertSampleFragment();
                clearStack = false;
                persistCurrent = true;
                break;
            }
            case TAKE_SAMPLE:
            default: {
                fragment = new TakeSampleFragment();
                clearStack = true;
                persistCurrent = true;
                break;
            }
        }

        if (clearStack) {
            clearFragmentStack();
        }
        switchToFragment(fragment, persistCurrent);
    }

    @Override
    public void navigateBack() {
        boolean navigationHandled = false;
        final BaseFragment activeBaseFragment = getActiveBaseFragment();
        if (activeBaseFragment instanceof TestFragmentBaseView) {
            navigationHandled = ((TestFragmentBaseView) activeBaseFragment).onNavigateBackPressed();
        }

        if (!navigationHandled) {
            onBackPressed();
        }
    }

    @Override
    public void navigateForward() {
        boolean navigationHandled = false;
        final BaseFragment activeBaseFragment = getActiveBaseFragment();
        if (activeBaseFragment instanceof TestFragmentBaseView) {
            navigationHandled = ((TestFragmentBaseView) activeBaseFragment).onNavigateForwardPressed();
        }

        if (!navigationHandled) {
            finish();
        }
    }
}
