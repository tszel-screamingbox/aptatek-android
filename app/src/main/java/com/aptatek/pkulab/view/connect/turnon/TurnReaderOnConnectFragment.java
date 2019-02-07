package com.aptatek.pkulab.view.connect.turnon;

import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.injection.module.scan.ScanModule;
import com.aptatek.pkulab.view.connect.ConnectReaderScreen;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnFragment;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenter;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnView;
import com.aptatek.pkulab.view.dialog.AlertDialogFragment;

import java.util.List;

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
        fragmentComponent.plus(new ScanModule()).inject(this);
    }

    @Override
    public void showDeviceNotSupported() {
        final AlertDialogModel alertDialogModel = AlertDialogModel.builder()
                .setTitle(getString(R.string.connect_turnon_notsupported_title))
                .setMessage(getString(R.string.connect_turnon_notsupported_message))
                .setCancelable(false)
                .setNeutralButtonText(getString(android.R.string.ok))
                .build();

        AlertDialogFragment.create(alertDialogModel, decision ->
            getActivity().finish()
        ).show(getChildFragmentManager(), "notsupported");
    }

    @Override
    public void requestMissingPermissions(@NonNull List<String> permissions) {
        // ??
    }

    @Override
    public void onActivityResumed() {
        // ??
    }

    @Override
    public void showScreen(@NonNull ConnectReaderScreen screen) {
        // ??
    }

    @Override
    public void navigateBack() {
        // ??
    }
}
