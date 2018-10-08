package com.aptatek.pkuapp.view.connect.turnon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.model.AlertDialogModel;
import com.aptatek.pkuapp.injection.component.FragmentComponent;
import com.aptatek.pkuapp.view.connect.common.BaseConnectScreenFragment;
import com.aptatek.pkuapp.view.dialog.AlertDialogDecisionListener;
import com.aptatek.pkuapp.view.dialog.AlertDialogDecisions;
import com.aptatek.pkuapp.view.dialog.AlertDialogFragment;
import com.aptatek.pkuapp.view.main.MainActivity;

import javax.inject.Inject;

import butterknife.OnClick;

public class TurnOnFragment extends BaseConnectScreenFragment<TurnOnView, TurnOnPresenter> implements TurnOnView {

    @Inject
    TurnOnPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_turn_on;
    }

    @Override
    protected void initObjects(final View view) {

    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @NonNull
    @Override
    public TurnOnPresenter createPresenter() {
        return presenter;
    }

    @OnClick(R.id.turnOnNext)
    public void onNextClick() {
        presenter.checkDeviceSupported();
    }

    @OnClick(R.id.turnOnSkip)
    public void onSkipClick() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
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
}
