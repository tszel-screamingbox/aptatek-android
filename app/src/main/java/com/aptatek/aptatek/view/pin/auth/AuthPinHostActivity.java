package com.aptatek.aptatek.view.pin.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseRootFrameActivity;
import com.aptatek.aptatek.view.pin.auth.add.AuthPinFragment;

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


