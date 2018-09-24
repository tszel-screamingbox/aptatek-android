package com.aptatek.pkuapp.view.pin.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.view.base.BaseActivity;
import com.aptatek.pkuapp.view.base.BaseRootFrameActivity;
import com.aptatek.pkuapp.view.main.MainActivity;
import com.aptatek.pkuapp.view.pin.auth.add.AuthPinFragment;

import javax.inject.Inject;

import activitystarter.ActivityStarter;
import activitystarter.Arg;


public class AuthPinHostActivity extends BaseRootFrameActivity<AuthPinHostActivityView, AuthPinHostActivityPresenter> implements AuthPinHostActivityView {

    @Arg(optional = true)
    boolean closeIfValid;

    @Inject
    AuthPinHostActivityPresenter presenter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStarter.fill(this, savedInstanceState);
        switchToFragment(new AuthPinFragment());
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        getActivityComponent().inject(this);
    }

    @NonNull
    @Override
    public AuthPinHostActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.id.rootFrame;
    }

    public void successfullyAuthorized() {
        if (closeIfValid) {
            setResult(RESULT_OK);
            finish();
        } else {
            final Intent intent = new Intent(this, MainActivity.class);
            launchActivity(intent, true, BaseActivity.Animation.RIGHT_TO_LEFT);
        }
    }
}


