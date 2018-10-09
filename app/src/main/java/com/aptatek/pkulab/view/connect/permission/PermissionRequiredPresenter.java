package com.aptatek.pkulab.view.connect.permission;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.aptatek.pkulab.injection.qualifier.ActivityContext;
import com.aptatek.pkulab.view.connect.common.BaseConnectScreenPresenter;
import com.aptatek.pkulab.view.connect.common.ConnectCommonView;

import javax.inject.Inject;

public class PermissionRequiredPresenter extends BaseConnectScreenPresenter<PermissionRequiredView> {

    @Inject
    public PermissionRequiredPresenter(@ActivityContext final Context context) {
        super(context);
    }

    @SuppressLint("NewApi")
    public void grantPermissions() {
        if (context instanceof AppCompatActivity) {
            final AppCompatActivity activityContext = (AppCompatActivity) this.context;
            final boolean showBluetoothRationale = activityContext.shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH);
            final boolean showLocationRationale = activityContext.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION);

            if (showBluetoothRationale || showLocationRationale) {
                requestMissingPermissions();
            } else {
                ifViewAttached(PermissionRequiredView::navigateToAppSettings);
            }
        }
    }

    @Override
    protected void onRequiredConditionsMet() {
        ifViewAttached(ConnectCommonView::navigateBack);
    }

    @Override
    protected void onMissingPermissionsFound() {

    }
}
