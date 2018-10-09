package com.aptatek.pkuapp.view.connect.connected;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.model.ReaderDevice;
import com.aptatek.pkuapp.injection.component.FragmentComponent;
import com.aptatek.pkuapp.view.connect.common.BaseConnectScreenFragment;
import com.aptatek.pkuapp.view.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ConnectedFragment extends BaseConnectScreenFragment<ConnectedView, ConnectedPresenter> implements ConnectedView {

    @Inject
    ConnectedPresenter connectedPresenter;

    @BindView(R.id.connectedDeviceName)
    TextView tvDevice;
    @BindView(R.id.connectedBatteryLevel)
    TextView tvBatteryLevel;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_connected;
    }

    @Override
    protected void initObjects(final View view) {

    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public void displayReaderDevice(@NonNull final ReaderDevice device, final int batteryLevel) {
        tvDevice.setText(device.getName());
        tvBatteryLevel.setText(String.valueOf(batteryLevel).concat("%"));
    }

    @NonNull
    @Override
    public ConnectedPresenter createPresenter() {
        return connectedPresenter;
    }

    @OnClick(R.id.connectedDisconnect)
    public void onDisconnectClicked() {
        Toast.makeText(getActivity(), "disconnect clicked", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.connectedButton)
    public void onGoHomeCliked() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }
}
