package com.aptatek.pkuapp.view.connect.scan.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aptatek.pkuapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanDeviceViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.scanDeviceName)
    TextView tvName;
    @BindView(R.id.scanDeviceAddress)
    TextView tvAddress;
    @BindView(R.id.scanConnectButton)
    View btnConnect;
    @BindView(R.id.scanConnectProgress)
    View pbConnecting;

    ScanDeviceViewHolder(final @NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bind(final @NonNull ScanDeviceAdapterItem adapterItem) {
        tvName.setText(adapterItem.getName());
        tvAddress.setText(adapterItem.getMacAddress());
    }

}
