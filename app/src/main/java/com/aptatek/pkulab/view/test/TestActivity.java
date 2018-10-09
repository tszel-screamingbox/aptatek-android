package com.aptatek.pkulab.view.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;
import com.aptatek.pkulab.view.test.breakfoil.BreakFoilFragment;
import com.aptatek.pkulab.view.test.canceltest.CancelTestFragment;
import com.aptatek.pkulab.view.test.collectblood.CollectBloodFragment;
import com.aptatek.pkulab.view.test.connectitall.ConnectItAllFragment;
import com.aptatek.pkulab.view.test.mixsample.MixSampleFragment;
import com.aptatek.pkulab.view.test.pokefingertip.PokeFingertipFragment;
import com.aptatek.pkulab.view.test.testing.TestingFragment;
import com.aptatek.pkulab.view.test.turnreaderon.TurnReaderOnFragment;
import com.aptatek.pkulab.view.test.wetting.WettingFragment;
import com.rd.PageIndicatorView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends BaseActivity<TestActivityView, TestActivityPresenter>
        implements TestActivityView {

    private static final String KEY_WETTING_FINISHED = "com.aptatek.wetting.finished";

    public static Intent createStarter(@NonNull final Context context) {
        return new Intent(context, TestActivity.class);
    }

    public static Bundle createForSampleWettingFinishedIntent() {
        final Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_WETTING_FINISHED, true);
        return bundle;
    }

    @Inject
    TestActivityPresenter testActivityPresenter;

    @BindView(R.id.testCancelButton)
    View cancelButton;
    @BindView(R.id.testNextButton)
    Button nextButton;
    @BindView(R.id.testProgress)
    ProgressBar testProgress;
    @BindView(R.id.testBattery)
    TextView battery;
    @BindView(R.id.bottomBar)
    ConstraintLayout bottomBar;
    @BindView(R.id.testPageIndicator)
    PageIndicatorView screenPagerIndicator;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        if (getIntent().getBooleanExtra(KEY_WETTING_FINISHED, false)) {
            showScreen(TestScreens.TURN_READER_ON);
        }

        screenPagerIndicator.setDynamicCount(false);
        screenPagerIndicator.setCount(TestScreens.values().length - 1); // Cancel screen is ignored
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
    void onNextClicked() {
        final BaseFragment activeBaseFragment = getActiveBaseFragment();
        if (activeBaseFragment instanceof TestBaseFragment) {
            final boolean willHandle = ((TestBaseFragment) activeBaseFragment).onNextPressed();
            if (!willHandle) {
                showNextScreen();
            }
        }
    }

    @Override
    public void setBottomBarVisible(final boolean visible) {
        bottomBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showScreen(@NonNull final TestScreens screen) {
        final TestBaseFragment fragment;

        switch (screen) {
            case CANCEL: {
                fragment = new CancelTestFragment();
                break;
            }
            case POKE_FINGERTIP: {
                fragment = new PokeFingertipFragment();
                break;
            }
            case COLLECT_BLOOD: {
                fragment = new CollectBloodFragment();
                break;
            }
            case MIX_SAMPLE: {
                fragment = new MixSampleFragment();
                break;
            }
            case WETTING: {
                fragment = new WettingFragment();
                break;
            }
            case TURN_READER_ON: {
                fragment = new TurnReaderOnFragment();
                break;
            }
            case CONNECT_IT_ALL: {
                fragment = new ConnectItAllFragment();
                break;
            }
            case TESTING: {
                fragment = new TestingFragment();
                break;
            }
            case BREAK_FOIL:
            default: {
                fragment = new BreakFoilFragment();
                break;
            }
        }

        switchToFragment(fragment);
        screenPagerIndicator.setSelection(screen.ordinal());
    }

    @Override
    public void showNextScreen() {
        presenter.onShowNextScreen(getCurrentScreen());
    }

    private TestScreens getCurrentScreen() {
        return ((TestBaseFragment) getActiveBaseFragment()).getScreen();
    }

    @Override
    public void showPreviousScreen() {
        super.onBackPressed();
        screenPagerIndicator.setSelection(getCurrentScreen().ordinal());
    }

    @Override
    public void onBackPressed() {
        presenter.onShowPreviousScreen(getCurrentScreen());
    }

    @Override
    public void setBatteryIndicatorVisible(final boolean visible) {
        battery.setVisibility(visible ? View.VISIBLE : View.GONE);
        nextButton.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void setBatteryPercentageText(final @NonNull String percentageText) {
        battery.setText(percentageText);
    }

    @Override
    public void setProgressVisible(final boolean visible) {
        testProgress.setVisibility(visible ? View.VISIBLE : View.GONE);
        screenPagerIndicator.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setProgressPercentage(final int percentage) {
        testProgress.setProgress(percentage);
    }
}