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
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisions;
import com.aptatek.pkulab.view.dialog.AlertDialogFragment;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;
import com.aptatek.pkulab.view.test.breakfoil.BreakFoilFragment;
import com.aptatek.pkulab.view.test.canceltest.CancelTestFragment;
import com.aptatek.pkulab.view.test.collectblood.CollectBloodFragment;
import com.aptatek.pkulab.view.test.connectitall.ConnectItAllFragment;
import com.aptatek.pkulab.view.test.mixsample.MixSampleFragment;
import com.aptatek.pkulab.view.test.pokefingertip.PokeFingertipFragment;
import com.aptatek.pkulab.view.test.selftest.SelfTestFragment;
import com.aptatek.pkulab.view.test.testing.TestingFragment;
import com.aptatek.pkulab.view.test.turnreaderon.TurnReaderOnFragment;
import com.aptatek.pkulab.view.test.wetting.WettingFragment;
import com.rd.PageIndicatorView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class TestActivity extends BaseActivity<TestActivityView, TestActivityPresenter>
        implements TestActivityView {

    private static final String TAG_BATTER_DIALOG = "aptatek.main.home.battery.dialog";

    public static Intent createStarter(@NonNull final Context context) {
        return new Intent(context, TestActivity.class);
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

        screenPagerIndicator.setDynamicCount(false);
        screenPagerIndicator.setCount(TestScreens.values().length - 1); // Cancel screen is ignored

    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.showProperScreen();
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.plus(new TestModule(), new RangeInfoModule())
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
        bottomBar.setVisibility(visible ? VISIBLE : GONE);
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
            case SELF_TEST: {
                fragment = new SelfTestFragment();
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
        presenter.checkBattery();
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
    public void showBatteryAlert() {
        final AlertDialogModel model = AlertDialogModel.builder()
                .setTitle(getString(R.string.home_battery_alert_title))
                .setMessage(getString(R.string.home_battery_alert_content))
                .setPositiveButtonText(getString(R.string.alertdialog_button_ok))
                .setCancelable(true)
                .build();

        final AlertDialogFragment dialogFragment = AlertDialogFragment.create(model, decision -> {
            if (decision == AlertDialogDecisions.POSITIVE) {
                finish();
            }
        });
        dialogFragment.setCancelable(false);
        dialogFragment.show(getSupportFragmentManager(), TAG_BATTER_DIALOG);
    }

    @Override
    public void setBatteryIndicatorVisible(final boolean visible) {
        battery.setVisibility(visible ? VISIBLE : GONE);
        nextButton.setVisibility(visible ? INVISIBLE : VISIBLE);
    }

    @Override
    public void setBatteryPercentageText(final @NonNull String percentageText) {
        battery.setText(percentageText);
    }

    @Override
    public void setProgressVisible(final boolean visible) {
        testProgress.setVisibility(visible ? VISIBLE : GONE);
        screenPagerIndicator.setVisibility(visible ? INVISIBLE : VISIBLE);
    }

    @Override
    public void setProgressPercentage(final int percentage) {
        testProgress.setProgress(percentage);
    }
}
