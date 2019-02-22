package com.aptatek.pkulab.view.connect.onboarding.turnon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.view.connect.onboarding.ConnectReaderScreen;
import com.aptatek.pkulab.view.connect.onboarding.ConnectReaderView;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnFragment;
import com.aptatek.pkulab.view.main.MainHostActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class TurnReaderOnConnectFragment extends TurnReaderOnFragment<TurnReaderOnConnectView, TurnReaderOnConnectPresenter> implements TurnReaderOnConnectView {

    @BindView(R.id.turnReaderOnSkip)
    View btnSkip;

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

    @Override
    public void onSelfCheckComplete() {
        presenter.syncData();
    }

    @Override
    public void navigateToHome() {
        getBaseActivity().launchActivity(new Intent(requireActivity(), MainHostActivity.class));
    }

    @Override
    public void displaySkipButton() {
        btnSkip.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.turnReaderOnSkip)
    void onSkipClicked() {
        presenter.syncData();
    }
}
