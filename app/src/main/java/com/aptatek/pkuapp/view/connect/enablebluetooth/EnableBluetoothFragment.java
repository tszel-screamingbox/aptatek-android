package com.aptatek.pkuapp.view.connect.enablebluetooth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.FragmentComponent;
import com.aptatek.pkuapp.view.connect.common.BaseConnectScreenFragment;

import javax.inject.Inject;

import butterknife.OnClick;

public class EnableBluetoothFragment extends BaseConnectScreenFragment<EnableBluetoothView, EnableBluetoothPresenter> implements EnableBluetoothView {

    @Inject
    EnableBluetoothPresenter presenter;

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
    }

    @OnClick(R.id.bluetoothButton)
    public void onEnableBluetoothClick() {
        final Intent intent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intent);
    }
}
