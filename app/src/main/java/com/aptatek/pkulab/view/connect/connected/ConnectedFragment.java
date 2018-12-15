package com.aptatek.pkulab.view.connect.connected;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.view.connect.common.BaseConnectScreenFragment;
import com.aptatek.pkulab.view.main.MainHostActivity;

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

    @Override
    public void displayWorkflowState(@NonNull WorkflowState workflowState) {
        Toast.makeText(getActivity(), workflowState.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displaySyncFinished(final int numResults) {
        Toast.makeText(getActivity(), "Successfully synced " + numResults + " records", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public ConnectedPresenter createPresenter() {
        return connectedPresenter;
    }

    @OnClick(R.id.connectedDisconnect)
    public void onDisconnectClicked() {
        presenter.disconnect();
    }

    @OnClick(R.id.connectedButton)
    public void onGoHomeClicked() {
        startActivity(new Intent(getActivity(), MainHostActivity.class));
        finish();
    }

    @Override
    public void finish() {
        getActivity().finish();
    }
}
