package com.aptatek.pkulab.view.connect.onboarding.permission;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.view.connect.permission.PermissionRequiredPresenter;
import com.aptatek.pkulab.view.connect.permission.PermissionRequiredPresenterImpl;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

public class PermissionRequiredOnConnectPresenter extends MvpBasePresenter<PermissionRequiredOnboardingView> implements PermissionRequiredPresenter<PermissionRequiredOnboardingView> {

    private final PermissionRequiredPresenterImpl wrapped;

    @Inject
    PermissionRequiredOnConnectPresenter(final BluetoothInteractor bluetoothInteractor, final IAnalyticsManager analyticsManager) {
        wrapped = new PermissionRequiredPresenterImpl(bluetoothInteractor, analyticsManager);
    }

    @Override
    public void attachView(@NonNull final PermissionRequiredOnboardingView view) {
        wrapped.attachView(view);
        super.attachView(view);
    }

    @Override
    public void detachView() {
        wrapped.detachView();
        super.detachView();
    }

    @Override
    public void checkPermissions() {
        wrapped.checkPermissions();
    }

    @Override
    public void evaluatePermissionResults(final List<PermissionResult> results) {
        wrapped.evaluatePermissionResults(results);
    }

    @Override
    public void logPermissionSettingsOpened() {
        wrapped.logPermissionSettingsOpened();
    }
}
