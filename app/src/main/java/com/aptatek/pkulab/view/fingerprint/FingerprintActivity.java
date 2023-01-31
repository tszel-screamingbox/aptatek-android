package com.aptatek.pkulab.view.fingerprint;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.connect.onboarding.ConnectOnboardingReaderActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FingerprintActivity extends BaseActivity<FingerprintActivityView, FingerprintActivityPresenter> implements FingerprintActivityView {

    @Inject
    FingerprintActivityPresenter presenter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        ButterKnife.bind(this);
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @NonNull
    @Override
    public FingerprintActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_fingerprint;
    }


    @OnClick(R.id.enableButton)
    public void onEnableButtonClicked() {
        presenter.enableFingerprintScan();
        launchActivity(ConnectOnboardingReaderActivity.starter(this), true, Animation.RIGHT_TO_LEFT);
    }

    @OnClick(R.id.disableButton)
    public void onDisableButtonClicked() {
        presenter.disableFingerprintScan();
        launchActivity(ConnectOnboardingReaderActivity.starter(this), true, Animation.RIGHT_TO_LEFT);
    }
}
