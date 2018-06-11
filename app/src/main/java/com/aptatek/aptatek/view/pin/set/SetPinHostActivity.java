package com.aptatek.aptatek.view.pin.set;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseRootFrameActivity;
import com.aptatek.aptatek.view.pin.set.add.AddPinFragment;

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
}


