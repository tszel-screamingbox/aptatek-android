package com.aptatek.pkuapp.view.connect.scan.adapter;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.model.ReaderDevice;
import com.aptatek.pkuapp.view.base.AdapterItem;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ScanDeviceAdapterItem implements AdapterItem {

    @NonNull
    public abstract String getName();

    @NonNull
    public abstract String getMacAddress();

    @Override
    public Object uniqueIdentifier() {
        return getMacAddress();
    }

    public static ScanDeviceAdapterItem create(@NonNull final ReaderDevice device) {
        return new AutoValue_ScanDeviceAdapterItem(device.getName(), device.getMac());
    }
}
