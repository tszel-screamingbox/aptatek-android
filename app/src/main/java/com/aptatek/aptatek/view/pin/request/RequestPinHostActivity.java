package com.aptatek.aptatek.view.pin.request;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseRootFrameActivity;
import com.aptatek.aptatek.view.pin.request.add.RequestPinFragment;

import javax.inject.Inject;


public class RequestPinHostActivity extends BaseRootFrameActivity<RequestPinHostActivityView, RequestPinHostActivityPresenter> implements RequestPinHostActivityView {

    @Inject
    RequestPinHostActivityPresenter presenter;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchToFragment(new RequestPinFragment());
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        getActivityComponent().inject(this);
    }

    @NonNull
    @Override
    public RequestPinHostActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.id.rootFrame;
    }
}


