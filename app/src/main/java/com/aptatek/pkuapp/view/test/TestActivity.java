package com.aptatek.pkuapp.view.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.view.View;
import android.widget.Button;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.injection.module.test.TestModule;
import com.aptatek.pkuapp.view.base.BaseActivity;
import com.aptatek.pkuapp.view.base.BaseFragment;
import com.aptatek.pkuapp.view.test.breakfoil.BreakFoilFragment;
import com.aptatek.pkuapp.view.test.canceltest.CancelTestFragment;

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

    @BindView(R.id.testCancelButton)
    View cancelButton;
    @BindView(R.id.testNextButton)
    Button nextButton;
    @BindView(R.id.testBottomBar)
    Group bottomBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        if (getIntent().getBooleanExtra(KEY_INCUBATION_FINISHED, false)) {
//            showScreen(TestScreens.INSERT_CASSETTE);
        } else if (getIntent().getBooleanExtra(KEY_SAMPLE_WETTING_FINISHED, false)) {
//            showScreen(TestScreens.CANCEL); // TODO implement start test screen
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

    @OnClick(R.id.testCancelButton)
    void onCancelClicked() {
        showScreen(TestScreens.CANCEL);
    }

    @OnClick(R.id.testNextButton)
    void onNavigationClicked() {
        showNextScreen();
    }

    @Override
    public void setNextButtonEnabled(final boolean enabled) {
        nextButton.setEnabled(enabled);
    }

    @Override
    public void setNextButtonVisible(final boolean visible) {
        nextButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setBottomBarVisible(final boolean visible) {
        bottomBar.setVisibility(visible ? View.VISIBLE : View.GONE);
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
            default: {
                fragment = new BreakFoilFragment();
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
    public void showNextScreen() {

    }

    @Override
    public void showPreviousScreen() {
        onBackPressed();
    }
}
