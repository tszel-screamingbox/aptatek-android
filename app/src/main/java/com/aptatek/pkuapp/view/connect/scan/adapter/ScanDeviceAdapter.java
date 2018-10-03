package com.aptatek.pkuapp.view.connect.scan.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.view.base.BaseAdapter;

public class ScanDeviceAdapter extends BaseAdapter<ScanDeviceViewHolder, ScanDeviceAdapterItem> {

    @Override
    protected ScanDeviceViewHolder setViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScanDeviceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan_device, parent, false));
    }

    @Override
    protected void bindData(@NonNull ScanDeviceViewHolder holder, int position) {
        holder.bind(data.getCurrentList().get(position));
    }
}
