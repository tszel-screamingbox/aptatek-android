package com.aptatek.pkulab.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.aptatek.pkulab.BuildConfig;
import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.connect.onboarding.ConnectOnboardingReaderActivity;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisions;
import com.aptatek.pkulab.view.dialog.AlertDialogFragment;
import com.aptatek.pkulab.view.parentalgate.ParentalGateActivity;
import com.aptatek.pkulab.view.pin.auth.AuthPinHostActivity;
import com.aptatek.pkulab.view.pin.set.SetPinHostActivity;
import com.uxcam.UXCam;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aptatek.pkulab.view.base.BaseActivity.Animation.FADE;

public class SplashActivity extends BaseActivity<SplashActivityView, SplashActivityPresenter> implements SplashActivityView {

    private static final String TAG_ROOT_DIALOG = "aptatek.splash.root.dialog";

    @Inject
    SplashActivityPresenter presenter;

    @BindView(R.id.logo)
    ImageView logoImageView;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        if (BuildConfig.FLAVOR.equals("prod")) {
            UXCam.startWithKey(BuildConfig.UXCAM_KEY);
        }

        if (getIntent().hasExtra(Constants.EXTRA_RESTART_NOTIFICATION_ERROR)) {
            presenter.logBtError();
        }
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @NonNull
    @Override
    public SplashActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return 0;
    }

    @Override
    public void onParentalGateShouldLoad() {
        launchActivity(ParentalGateActivity.starter(this), true, FADE);
    }

    @Override
    public void onRequestPinActivityShouldLoad() {
        final Intent intent = new Intent(this, AuthPinHostActivity.class);
        launchActivity(intent, true, FADE);
    }

    @Override
    public void onSetPinActivityShouldLoad() {
        final Intent intent = new Intent(this, SetPinHostActivity.class);
        launchActivity(intent, true, FADE);
    }

    @Override
    public void onConnectReaderShouldLoad() {
        launchActivity(ConnectOnboardingReaderActivity.starter(this), true, FADE);
    }

    @Override
    public void showAlertDialog(@StringRes int title, @StringRes int message) {
        final AlertDialogModel model = AlertDialogModel.builder()
                .setTitle(getString(title))
                .setMessage(getString(message))
                .setNegativeButtonText(getString(R.string.alertdialog_button_no))
                .setPositiveButtonText(getString(R.string.alertdialog_button_yes))
                .setCancelable(false)
                .build();

        final AlertDialogFragment dialogFragment = AlertDialogFragment.create(
                model,
                decision -> {
                    if (decision == AlertDialogDecisions.POSITIVE) {
                        presenter.switchToNextActivity();
                    } else {
                        finish();
                    }
                });
        dialogFragment.show(getSupportFragmentManager(), TAG_ROOT_DIALOG);
    }

    @Override
    protected boolean shouldShowPinAuthWhenInactive() {
        return false;
    }
}
