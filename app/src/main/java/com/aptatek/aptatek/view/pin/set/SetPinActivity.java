package com.aptatek.aptatek.view.pin.set;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseRootFrameActivity;
import com.aptatek.aptatek.view.pin.set.add.AddPinFragment;

import javax.inject.Inject;

import activitystarter.ActivityStarter;
import activitystarter.Arg;


public class SetPinActivity extends BaseRootFrameActivity<SetPinActivityView, SetPinActivityPresenter> implements SetPinActivityView {

    @Arg
    String as;

    @Inject
    SetPinActivityPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStarter.fill(this, savedInstanceState);
        switchToFragment(new AddPinFragment());
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        getActivityComponent().inject(this);
    }

    @NonNull
    @Override
    public SetPinActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.id.rootFrame;
    }
}


