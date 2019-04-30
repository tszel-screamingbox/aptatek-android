package com.aptatek.pkulab.view.connect.permission;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.aptatek.pkulab.view.connect.common.BaseConnectView;

public interface PermissionRequiredView extends BaseConnectView, LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onActivityResumed();

    void onConditionsMet();

}
