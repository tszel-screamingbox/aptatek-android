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
    }

    @OnClick(R.id.scanDeviceName)
    void onClickConnect() {
        if (connectClickListener != null) {
            connectClickListener.onConnectClick(adapterItem);
        }
    }

}
