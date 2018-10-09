package com.aptatek.pkuapp.view.connect.turnon;

import android.content.Context;
import android.content.pm.PackageManager;

import com.aptatek.pkuapp.injection.qualifier.ActivityContext;
import com.aptatek.pkuapp.view.connect.ConnectReaderScreen;
import com.aptatek.pkuapp.view.connect.common.BaseConnectScreenPresenter;

import javax.inject.Inject;

public class TurnOnPresenter extends BaseConnectScreenPresenter<TurnOnView> {

    @Inject
    public TurnOnPresenter(@ActivityContext final Context context) {
        super(context);
    }

    @Override
    protected void onRequiredConditionsMet() {
        ifViewAttached(attachedView -> attachedView.showScreen(ConnectReaderScreen.SCAN));
    }

    @Override
    protected void onMissingPermissionsFound() {
        requestMissingPermissions();
    }

    public void checkDeviceSupported() {
        final boolean isBleSupported = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        if (!isBleSupported) {
            ifViewAttached(TurnOnView::showDeviceNotSupported);
        } else {
            checkMandatoryRequirements();
        }
    }
}
