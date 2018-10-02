package com.aptatek.pkuapp.view.connect.enablebluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.FragmentComponent;
import com.aptatek.pkuapp.view.connect.common.BaseConnectScreenFragment;

import javax.inject.Inject;

import butterknife.OnClick;

public class EnableBluetoothFragment extends BaseConnectScreenFragment<EnableBluetoothView, EnableBluetoothPresenter> implements EnableBluetoothView {

    private static final int REQ_BLUETOOTH = 258;

    @Inject
    EnableBluetoothPresenter presenter;

    private final BroadcastReceiver btReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (presenter.isBluetoothEnabled()) {
                navigateBack();
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_bluetooth;
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
    public EnableBluetoothPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.checkMandatoryRequirements();

        getActivity().registerReceiver(btReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(btReceiver);

        super.onStop();
    }

    @OnClick(R.id.bluetoothButton)
    public void onEnableBluetoothClick() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, REQ_BLUETOOTH);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQ_BLUETOOTH && presenter.isBluetoothEnabled()) {
            navigateBack();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
