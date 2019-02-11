package com.aptatek.pkulab.view.connect.permission;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

public class PermissionRequiredPresenter extends MvpBasePresenter<PermissionRequiredView> {

    @Inject
    public PermissionRequiredPresenter() {
    }

//    @SuppressLint("NewApi")
//    public void grantPermissions() {
//        if (context instanceof AppCompatActivity) {
//            final AppCompatActivity activityContext = (AppCompatActivity) this.context;
//            final boolean showBluetoothRationale = activityContext.shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH);
//            final boolean showLocationRationale = activityContext.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION);
//
//            if (showBluetoothRationale || showLocationRationale) {
//                requestMissingPermissions();
//            } else {
//                ifViewAttached(PermissionRequiredView::navigateToAppSettings);
//            }
//        }
//    }

}
