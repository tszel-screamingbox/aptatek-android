package com.aptatek.pkulab.view.connect.turnon;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.view.connect.ConnectReaderScreen;
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
    public void showScreen(@NonNull final ConnectReaderScreen screen) {
        // ??
    }

    @Override
    public void navigateBack() {
        // ??
    }
}
