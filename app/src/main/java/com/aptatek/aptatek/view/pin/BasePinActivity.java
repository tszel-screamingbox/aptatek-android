package com.aptatek.aptatek.view.pin;

import android.os.Bundle;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.base.IActivityComponentProvider;

public abstract class BasePinActivity extends BaseActivity implements IActivityComponentProvider {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectActivity(activityComponent());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
    }


}
