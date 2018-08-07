package com.aptatek.pkuapp.view.splash;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.view.base.BaseActivity;
import com.aptatek.pkuapp.view.parentalgate.ParentalGateActivity;
import com.aptatek.pkuapp.view.pin.auth.AuthPinHostActivity;
import com.aptatek.pkuapp.view.pin.set.SetPinHostActivity;

import javax.inject.Inject;

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
