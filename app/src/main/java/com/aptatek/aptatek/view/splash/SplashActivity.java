package com.aptatek.aptatek.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.parentalgate.ParentalGateActivity;
import com.aptatek.aptatek.view.pin.auth.AuthPinHostActivity;
import com.aptatek.aptatek.view.pin.set.SetPinHostActivity;

import javax.inject.Inject;

import timber.log.Timber;

public class SplashActivity extends BaseActivity<SplashActivityView, SplashActivityPresenter> implements SplashActivityView {

    @Inject
    SplashActivityPresenter presenter;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("");
    }

    @Override
    public int getFrameLayoutId() {
        return 0;
    }

    @Override
    public void onParentalGateShouldLoad() {
        launchActivity(ParentalGateActivity.starter(this), true, Animation.RIGHT_TO_LEFT);
    }

    @Override
    public void onRequestPinActivityShouldLoad() {
        final Intent intent = new Intent(this, AuthPinHostActivity.class);
        launchActivity(intent, true, Animation.RIGHT_TO_LEFT);
    }

    @Override
    public void onSetPinActivityShouldLoad() {
        final Intent intent = new Intent(this, SetPinHostActivity.class);
        launchActivity(intent, true, Animation.RIGHT_TO_LEFT);
    }
}
