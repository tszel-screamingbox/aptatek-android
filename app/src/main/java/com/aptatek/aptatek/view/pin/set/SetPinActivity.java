package com.aptatek.aptatek.view.pin.set;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.pin.BasePinActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class SetPinActivity extends BasePinActivity implements SetPinActivityView {

    @Inject
    SetPinActivityPresenter presenter;


    @Override
    protected void finishedTyping(PinCode pinCode) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void injectActivity(ActivityComponent activityComponent) {
        getActivityComponent().inject(this);
    }

    @NonNull
    @Override
    public SetPinActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_pin;
    }

}


