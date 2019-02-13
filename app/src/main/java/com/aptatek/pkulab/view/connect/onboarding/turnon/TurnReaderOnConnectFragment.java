package com.aptatek.pkulab.view.connect.onboarding.turnon;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.view.connect.onboarding.ConnectReaderScreen;
import com.aptatek.pkulab.view.connect.onboarding.ConnectReaderView;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnFragment;

import javax.inject.Inject;

public class TurnReaderOnConnectFragment extends TurnReaderOnFragment<TurnReaderOnConnectView, TurnReaderOnConnectPresenter> implements TurnReaderOnConnectView {

    @Inject
    TurnReaderOnConnectPresenter presenter;

    @NonNull
    @Override
    public TurnReaderOnConnectPresenter createPresenter() {
        return presenter;
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public void displayMissingPermissions() {
        showScreen(ConnectReaderScreen.PERMISSION_REQUIRED);
    }

    @Override
    public void showScreen(@NonNull final ConnectReaderScreen screen) {
        final FragmentActivity activity = getActivity();
        if (activity instanceof ConnectReaderView) {
            ((ConnectReaderView) activity).showScreen(screen);
        }
    }

    @Override
    public void navigateBack() {
        final FragmentActivity activity = getActivity();
        if (activity instanceof ConnectReaderView) {
            ((ConnectReaderView) activity).navigateBack();
        }
    }
}
