package com.aptatek.pkuapp.view.fingerprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.view.base.BaseActivity;
import com.aptatek.pkuapp.view.main.MainActivity;

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
        final Intent intent = new Intent(this, MainActivity.class);
        launchActivity(intent, true, Animation.RIGHT_TO_LEFT);
    }

    @OnClick(R.id.disableButton)
    public void onDisableButtonClicked() {
        presenter.disableFingerprintScan();
        final Intent intent = new Intent(this, MainActivity.class);
        launchActivity(intent, true, Animation.RIGHT_TO_LEFT);
    }
}
