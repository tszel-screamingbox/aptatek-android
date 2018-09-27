package com.aptatek.pkuapp.view.connect.scan;

import android.content.Context;

import com.aptatek.pkuapp.injection.qualifier.ActivityContext;
import com.aptatek.pkuapp.view.connect.common.BaseConnectScreenPresenter;

import javax.inject.Inject;

public class ScanPresenter extends BaseConnectScreenPresenter<ScanView> {

    @Inject
    public ScanPresenter(@ActivityContext final Context context) {
        super(context);
    }

    @Override
    protected void onRequiredConditionsMet() {

    }

    @Override
    protected void onMissingPermissionsFound() {

    }
}
