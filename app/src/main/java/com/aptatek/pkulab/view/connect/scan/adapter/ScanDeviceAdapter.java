package com.aptatek.pkulab.view.connect.scan.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.view.base.BaseAdapter;

public class ScanDeviceAdapter extends BaseAdapter<ScanDeviceViewHolder, ScanDeviceAdapterItem> {

    private ConnectClickListener connectClickListener;

    @Override
    protected ScanDeviceViewHolder setViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new ScanDeviceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan_device, parent, false), connectClickListener);
    }

    @Override
    protected void bindData(@NonNull final ScanDeviceViewHolder holder, final int position) {
        holder.bind(data.getCurrentList().get(position));
    }

    public void setConnectClickListener(@NonNull final ConnectClickListener connectClickListener) {
        this.connectClickListener = connectClickListener;
    }
}
