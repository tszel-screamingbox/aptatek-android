package com.aptatek.pkulab.view.connect.enablebluetooth;

import android.content.Context;

import com.aptatek.pkulab.injection.qualifier.ActivityContext;
import com.aptatek.pkulab.view.connect.ConnectReaderScreen;
import com.aptatek.pkulab.view.connect.common.BaseConnectScreenPresenter;

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
