package com.aptatek.pkuapp.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.view.base.BaseActivity;
import com.aptatek.pkuapp.view.parentalgate.ParentalGateActivity;
import com.aptatek.pkuapp.view.pin.auth.AuthPinHostActivity;
import com.aptatek.pkuapp.view.pin.set.SetPinHostActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aptatek.pkuapp.view.base.BaseActivity.Animation.FADE;

public class SplashActivity extends BaseActivity<SplashActivityView, SplashActivityPresenter> implements SplashActivityView {

    @Inject
    SplashActivityPresenter presenter;

    @BindView(R.id.logo)
    ImageView logoImageView;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
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
}
