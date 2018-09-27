package com.aptatek.pkuapp.view.connect.enablebluetooth;

import android.content.Context;

import com.aptatek.pkuapp.injection.qualifier.ActivityContext;
import com.aptatek.pkuapp.view.connect.ConnectReaderScreen;
import com.aptatek.pkuapp.view.connect.common.BaseConnectScreenPresenter;

import javax.inject.Inject;

public class EnableBluetoothPresenter extends BaseConnectScreenPresenter<EnableBluetoothView> {

    @Inject
    public EnableBluetoothPresenter(@ActivityContext final Context context) {
        super(context);
    }

    @Override
    protected void onRequiredConditionsMet() {
        ifViewAttached(attachedView -> attachedView.showScreen(ConnectReaderScreen.TURN_ON));
    }

    @Override
    protected void onMissingPermissionsFound() {
        ifViewAttached(attachedView -> attachedView.showScreen(ConnectReaderScreen.PERMISSION_REQUIRED));
    }
}
