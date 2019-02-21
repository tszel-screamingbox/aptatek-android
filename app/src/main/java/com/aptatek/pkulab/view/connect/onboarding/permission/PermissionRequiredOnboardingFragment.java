package com.aptatek.pkulab.view.connect.onboarding.permission;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.view.connect.onboarding.ConnectReaderScreen;
import com.aptatek.pkulab.view.connect.onboarding.common.BaseConnectOnboardingScreenView;
import com.aptatek.pkulab.view.connect.permission.PermissionRequiredFragment;

import javax.inject.Inject;

public class PermissionRequiredOnboardingFragment extends PermissionRequiredFragment<PermissionRequiredOnboardingView, PermissionRequiredOnConnectPresenter> implements PermissionRequiredOnboardingView {

    @Inject
    PermissionRequiredOnConnectPresenter presenter;

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onConditionsMet() {
        navigateBack();
    }

    @NonNull
    @Override
    public PermissionRequiredOnConnectPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void showScreen(@NonNull final ConnectReaderScreen screen) {
        final FragmentActivity activity = getActivity();
        if (activity instanceof BaseConnectOnboardingScreenView) {
            ((BaseConnectOnboardingScreenView) activity).showScreen(screen);
        }
    }

    @Override
    public void navigateBack() {
        final FragmentActivity activity = getActivity();
        if (activity instanceof BaseConnectOnboardingScreenView) {
            ((BaseConnectOnboardingScreenView) activity).navigateBack();
        }
    }
}
