package com.aptatek.pkulab.view.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aptatek.pkulab.AptatekApplication;
import com.aptatek.pkulab.BuildConfig;
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
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;
import com.aptatek.pkulab.view.test.breakfoil.BreakFoilFragment;
import com.aptatek.pkulab.view.test.canceltest.CancelTestFragment;
import com.aptatek.pkulab.view.test.collectblood.CollectBloodFragment;
import com.aptatek.pkulab.view.test.connectitall.ConnectItAllFragment;
import com.aptatek.pkulab.view.test.mixsample.MixSampleFragment;
import com.aptatek.pkulab.view.test.pokefingertip.PokeFingertipFragment;
import com.aptatek.pkulab.view.test.testcomplete.TestCompleteFragment;
import com.aptatek.pkulab.view.test.testing.TestingFragment;
import com.aptatek.pkulab.view.test.turnreaderon.TurnReaderOnTestFragment;
import com.aptatek.pkulab.view.test.wetting.WettingFragment;
import com.aptatek.pkulab.widget.BatteryView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.rd.PageIndicatorView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.aptatek.pkulab.view.test.TestScreens.CANCEL;
import static com.aptatek.pkulab.view.test.TestScreens.TESTING;
import static com.aptatek.pkulab.view.test.TestScreens.showDotFor;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.from;

public class TestActivity extends BaseActivity<TestActivityView, TestActivityPresenter>
        implements TestActivityView {

    private static final String TAG_BATTER_DIALOG = "aptatek.main.home.battery.dialog";
    private static final String TAG_CURRENT_FRAGMENT = "aptatek.test.current.fragment";
    private static final String EXTRA_NOTIF_REASON = "aptatek.notif.reason";

    public static Intent createStarter(@NonNull final Context context) {
        return new Intent(context, TestActivity.class);
    }

    public static Intent createStarterForNotificationWithReason(@NonNull final Context context, final String reason) {
        final Intent intent = new Intent(context, TestActivity.class);
        intent.putExtra(EXTRA_NOTIF_REASON, reason);
        return intent;
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
    BatteryView battery;
    @BindView(R.id.bottomBar)
    ConstraintLayout bottomBar;
    @BindView(R.id.testPageIndicator)
    PageIndicatorView screenPagerIndicator;
    @BindView(R.id.bottom_sheet)
    ConstraintLayout bottomConstraintLayout;

    @BindView(R.id.testDisclaimerText)
    @Nullable
    protected TextView tvDisclaimer;
    @BindView(R.id.testDisclaimer)
    @Nullable
    protected ConstraintLayout disclaimerContainer;

    private boolean inForeground = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        screenPagerIndicator.setDynamicCount(false);
        //screenPagerIndicator.setCount(showDotFor().size());

        if (getIntent().hasExtra(EXTRA_NOTIF_REASON)) {
            presenter.logOpenFromNotification(getIntent().getStringExtra(EXTRA_NOTIF_REASON));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        inForeground = true;

        if (!AptatekApplication.get(this).shouldRequestPin()) {
            presenter.showProperScreen();
        }
    }

    @Override
    protected void onStop() {
        inForeground = false;

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        presenter.storeDestroyTimestamp();

        super.onDestroy();
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
        showScreen(CANCEL);
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

    @OnTouch(R.id.testDisclaimerText)
    public boolean disclaimerTouched(final MotionEvent event) {
        final BaseFragment activeBaseFragment = getActiveBaseFragment();
        if (activeBaseFragment instanceof WettingFragment) {
            return ((WettingFragment) activeBaseFragment).warningTextTouched(event);
        }
        return true;
    }

    @OnClick(R.id.testBattery)
    void onBatteryClicked() {
        if (BuildConfig.FLAVOR.equals("mock")) {
            showNextScreen();
        }
    }

    @Override
    public void setBottomBarVisible(final boolean visible) {
        bottomBar.setVisibility(visible ? VISIBLE : GONE);
    }

    @Override
    public void showScreen(@NonNull final TestScreens screen) {
        final BaseFragment fragment;
        boolean addToBackStack = true;
        boolean withAnimation = true;

        switch (screen) {
            case CANCEL: {
                fragment = CancelTestFragment.createCancelFragment(getCurrentScreen() == TESTING);
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
                addToBackStack = false;
                break;
            }
            case TURN_READER_ON: {
                fragment = new TurnReaderOnTestFragment();
                withAnimation = false;
                addToBackStack = false;
                break;
            }
            case CONNECT_IT_ALL: {
                fragment = new ConnectItAllFragment();
                addToBackStack = false;
                break;
            }
            case TESTING: {
                fragment = new TestingFragment();
                addToBackStack = false;
                break;
            }
            case TEST_COMPLETE: {
                fragment = new TestCompleteFragment();
                addToBackStack = false;
                break;
            }
            default: {
                fragment = new BreakFoilFragment();
                break;
            }
        }

        showFragment(fragment, addToBackStack, withAnimation);
        //screenPagerIndicator.setSelection(Math.min(screen.ordinal(), showDotFor().size()));
        presenter.checkBattery(screen);
    }

    private void showFragment(final Fragment fragment, final boolean addToBackStack, final boolean withAnimation) {
        if (!inForeground) return;

        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction transaction = fm.beginTransaction();
        if (withAnimation) {
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }

        final Fragment current = fm.findFragmentByTag(TAG_CURRENT_FRAGMENT);
        if (current != null && current.getClass().equals(fragment.getClass())) {
            return;
        }

        transaction.replace(getFrameLayoutId(), fragment, TAG_CURRENT_FRAGMENT);
        if (addToBackStack) {
            transaction.addToBackStack(fragment instanceof TestFragmentBaseView ? String.valueOf(((TestFragmentBaseView) fragment).getScreen().ordinal()) : null);
        }

        transaction.commit();
        fm.executePendingTransactions();
    }

    @Override
    public void showTurnReaderOn() {
        showFragment(TurnReaderOnTestFragment.create(getCurrentScreen()), false, false);
    }

    @Override
    public void showNextScreen() {
        presenter.onShowNextScreen(getCurrentScreen());
    }

    private TestScreens getCurrentScreen() {
        return ((TestFragmentBaseView) getActiveBaseFragment()).getScreen();
    }

    private void onBackPressedHere() {
        final FragmentManager fm = getSupportFragmentManager();
        fm.executePendingTransactions();

        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public TestScreens getPreviousScreen() {
        final FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            final FragmentManager.BackStackEntry backStackEntryAt = fm.getBackStackEntryAt(Math.max(0, fm.getBackStackEntryCount() - 1));
            try {
                final int ordinal = Integer.parseInt(backStackEntryAt.getName());
                return TestScreens.values()[ordinal];
            } catch (NumberFormatException e) {
                Timber.d("Failed to get previous screen");
            }
        }

        return TestScreens.TURN_READER_ON;
    }

    @Override
    public void showPreviousScreen() {
        onBackPressedHere();
        //screenPagerIndicator.setSelection(Math.min(getCurrentScreen().ordinal(), showDotFor().size()));
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
    public void setBatteryPercentage(final int percentage) {
        battery.setBatteryLevel(percentage);
    }

    @Override
    public void setProgressVisible(final boolean visible) {
        // testProgress.setVisibility(visible ? VISIBLE : GONE);
        // screenPagerIndicator.setVisibility(visible ? INVISIBLE : VISIBLE);
    }

    @Override
    public void setProgressPercentage(final int percentage) {
        testProgress.setProgress(percentage);
    }

    @Override
    public void setDisclaimerViewVisible(final boolean visible) {
        if (disclaimerContainer != null) {
            disclaimerContainer.setVisibility(visible ? VISIBLE : GONE);
        }
    }

    @Override
    public void setDisclaimerMessage(@NonNull final String message) {
        if (tvDisclaimer != null) {
            tvDisclaimer.setText(message);
        }
    }

    @Override
    public void setNextButtonVisible(final boolean visible) {
        nextButton.setVisibility(visible ? VISIBLE : INVISIBLE);
    }

    public void showHelpScreen() {
        final BottomSheetBehavior behavior = from(bottomConstraintLayout);
        behavior.setState(STATE_EXPANDED);
    }

    public void closeHelpScreen() {
        final BottomSheetBehavior behavior = from(bottomConstraintLayout);
        behavior.setState(STATE_COLLAPSED);
    }
}
