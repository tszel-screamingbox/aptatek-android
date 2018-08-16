package com.aptatek.pkuapp.view.pin.set;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.view.base.BaseRootFrameActivity;
import com.aptatek.pkuapp.view.base.idle.SimpleIdlingResource;
import com.aptatek.pkuapp.view.pin.set.add.AddPinFragment;

import javax.inject.Inject;


public class SetPinHostActivity extends BaseRootFrameActivity<SetPinHostActivityView, SetPinHostActivityPresenter> implements SetPinHostActivityView {


    @Inject
    SetPinHostActivityPresenter presenter;

    @Nullable
    public SimpleIdlingResource idlingResource;

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

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }
}


