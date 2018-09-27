package com.aptatek.pkuapp.view.connect.turnon;

import android.content.Context;

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
}
