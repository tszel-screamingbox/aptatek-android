package com.aptatek.pkulab.view.connect.scan.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aptatek.pkulab.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanDeviceViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.scanDeviceName)
    TextView tvName;
    @BindView(R.id.scanDeviceAddress)
    TextView tvAddress;
    @BindView(R.id.scanConnectButton)
    View btnConnect;
    @BindView(R.id.scanConnectProgress)
    View pbConnecting;
    @BindView(R.id.scanDeviceBlur)
    View vBlur;

    private ConnectClickListener connectClickListener;
    private ScanDeviceAdapterItem adapterItem;

    ScanDeviceViewHolder(final @NonNull View itemView, final @NonNull ConnectClickListener connectClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.connectClickListener = connectClickListener;
    }

    void bind(final @NonNull ScanDeviceAdapterItem adapterItem) {
        this.adapterItem = adapterItem;
        tvName.setText(adapterItem.getName());
        tvAddress.setText(adapterItem.getMacAddress());

        btnConnect.setVisibility(adapterItem.isConnectingToThis() ? View.GONE : View.VISIBLE);
        pbConnecting.setVisibility(adapterItem.isConnectingToThis() ? View.VISIBLE : View.GONE);

        vBlur.setVisibility(adapterItem.isEnabled() || adapterItem.isConnectingToThis() ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.scanConnectButton)
    void onClickConnect() {
        if (connectClickListener != null) {
            connectClickListener.onConnectClick(adapterItem);
        }
    }

}
