package com.aptatek.pkulab.view.pin.set;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.view.base.BaseRootFrameActivity;
import com.aptatek.pkulab.view.pin.set.add.AddPinFragment;

import javax.inject.Inject;


public class SetPinHostActivity extends BaseRootFrameActivity<SetPinHostActivityView, SetPinHostActivityPresenter> implements SetPinHostActivityView {


    @Inject
    SetPinHostActivityPresenter presenter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchToFragment(new AddPinFragment());
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        getActivityComponent().inject(this);
    }

    @NonNull
    @Override
    public SetPinHostActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.id.rootFrame;
    }

    @Override
    protected boolean shouldShowPinAuthWhenInactive() {
        return false;
    }

    @Override
    public void onValidPinTyped() {
        presenter.onPinSet();
    }

    @Override
    public void onInvalidPinTyped() {
        presenter.onPinSetFailed();
    }
}


