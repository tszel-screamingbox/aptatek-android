package com.aptatek.pkulab.view.connect.permission;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.aptatek.pkulab.view.connect.common.BaseConnectView;

public interface PermissionRequiredView extends BaseConnectView, LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onActivityResumed();

    void onConditionsMet();

}
