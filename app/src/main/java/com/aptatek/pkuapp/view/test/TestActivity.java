package com.aptatek.pkuapp.view.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.injection.module.test.TestModule;
import com.aptatek.pkuapp.view.base.BaseActivity;
import com.aptatek.pkuapp.view.base.BaseFragment;
import com.aptatek.pkuapp.view.test.base.TestBaseFragment;
import com.aptatek.pkuapp.view.test.breakfoil.BreakFoilFragment;
import com.aptatek.pkuapp.view.test.canceltest.CancelTestFragment;
import com.aptatek.pkuapp.view.test.collectblood.CollectBloodFragment;
import com.aptatek.pkuapp.view.test.connectitall.ConnectItAllFragment;
import com.aptatek.pkuapp.view.test.mixsample.MixSampleFragment;
import com.aptatek.pkuapp.view.test.pokefingertip.PokeFingertipFragment;
import com.aptatek.pkuapp.view.test.testing.TestingFragment;
import com.aptatek.pkuapp.view.test.turnreaderon.TurnReaderOnFragment;
import com.aptatek.pkuapp.view.test.wetting.WettingFragment;

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
    @BindView(R.id.testProgress)
    ProgressBar testProgress;
    @BindView(R.id.testBattery)
    TextView battery;
    @BindView(R.id.bottomBar)
    ConstraintLayout bottomBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            testProgress.getProgressDrawable().setColorFilter(ContextCompat.getColor(this, R.color.applicationGreen), PorterDuff.Mode.SRC_IN);
        }

        showScreen(TestScreens.BREAK_FOIL);

//        if (getIntent().getBooleanExtra(KEY_INCUBATION_FINISHED, false)) {
//            showScreen(TestScreens.INSERT_CASSETTE);
//        } else if (getIntent().getBooleanExtra(KEY_SAMPLE_WETTING_FINISHED, false)) {
//            showScreen(TestScreens.CANCEL);
//        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        presenter.showProperScreen(getActiveBaseFragment() != null);
//    }

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
    }

    @Override
    public void showNextScreen() {
        presenter.onShowNextScreen(((TestBaseFragment) getActiveBaseFragment()).getScreen());
    }

    @Override
    public void showPreviousScreen() {
        onBackPressed();
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
    }

    @Override
    public void setProgressPercentage(final int percentage) {
        testProgress.setProgress(percentage);
    }
}
