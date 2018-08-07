package com.aptatek.pkuapp.view.pin.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.view.base.BaseRootFrameActivity;
import com.aptatek.pkuapp.view.pin.auth.add.AuthPinFragment;

import javax.inject.Inject;


public class AuthPinHostActivity extends BaseRootFrameActivity<AuthPinHostActivityView, AuthPinHostActivityPresenter> implements AuthPinHostActivityView {

    @Inject
    AuthPinHostActivityPresenter presenter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}


