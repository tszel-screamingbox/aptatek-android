package com.aptatek.pkuapp.view.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.injection.module.test.TestModule;
import com.aptatek.pkuapp.view.base.BaseActivity;
import com.aptatek.pkuapp.view.base.BaseFragment;
import com.aptatek.pkuapp.view.test.base.TestFragmentBaseView;
import com.aptatek.pkuapp.view.test.canceltest.CancelTestFragment;
import com.aptatek.pkuapp.view.test.incubation.IncubationFragment;
import com.aptatek.pkuapp.view.test.samplewetting.SampleWettingFragment;
import com.aptatek.pkuapp.view.test.takesample.TakeSampleFragment;
import com.aptatek.pkuapp.view.test.tutorial.attachcube.AttachCubeFragment;
import com.aptatek.pkuapp.view.test.tutorial.insertcasette.InsertCasetteFragment;
import com.aptatek.pkuapp.view.test.tutorial.insertsample.InsertSampleFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends BaseActivity<TestActivityView, TestActivityPresenter>
    implements TestActivityView {

    private static final String KEY_INCUBATION_FINISHED = "com.aptatek.incubation.finished";
    private static final String KEY_SAMPLE_WETTING_FINISHED = "com.aptatek.samplewetting.finished";

    public static Intent createStarter(@NonNull final Context context) {
        return new Intent(context, TestActivity.class);
    }

    public static Bundle createForIncubationFinishedIntent() {
        final Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_INCUBATION_FINISHED, true);
        return bundle;
    }

    public static Bundle createForSampleWettingFinishedIntent() {
        final Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_SAMPLE_WETTING_FINISHED, true);
        return bundle;
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

        if (getIntent().getBooleanExtra(KEY_INCUBATION_FINISHED, false)) {
            showScreen(TestScreens.INSERT_CASSETTE);
        } else if (getIntent().getBooleanExtra(KEY_SAMPLE_WETTING_FINISHED, false)) {
            showScreen(TestScreens.CANCEL); // TODO implement start test screen
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.showProperScreen(getActiveBaseFragment() != null);
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

        switch (screen) {
            case CANCEL: {
                fragment = new CancelTestFragment();
                clearStack = false;
                break;
            }
            case INCUBATION: {
                fragment = new IncubationFragment();
                clearStack = false;
                break;
            }
            case INSERT_CASSETTE: {
                fragment = new InsertCasetteFragment();
                clearStack = false;
                break;
            }
            case ATTACH_CUBE: {
                fragment = new AttachCubeFragment();
                clearStack = false;
                break;
            }
            case INSERT_SAMPLE: {
                fragment = new InsertSampleFragment();
                clearStack = false;
                break;
            }
            case SAMPLE_WETTING: {
                fragment = new SampleWettingFragment();
                clearStack = true;
                break;
            }
            case TAKE_SAMPLE:
            default: {
                fragment = new TakeSampleFragment();
                clearStack = true;
                break;
            }
        }

        if (clearStack) {
            clearFragmentStack();
        }

        switchToFragment(fragment);
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
