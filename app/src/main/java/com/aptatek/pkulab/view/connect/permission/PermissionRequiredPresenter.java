package com.aptatek.pkulab.view.connect.permission;

import com.aptatek.pkulab.view.connect.common.BaseConnectPresenter;

import java.util.List;

public interface PermissionRequiredPresenter<V extends PermissionRequiredView> extends BaseConnectPresenter<V> {

    void evaluatePermissionResults(List<PermissionResult> results);

    void logPermissionSettingsOpened();

}
