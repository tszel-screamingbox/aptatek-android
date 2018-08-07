package com.aptatek.pkuapp.view.pin.set;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.view.base.BaseRootFrameActivity;
import com.aptatek.pkuapp.view.pin.set.add.AddPinFragment;

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


