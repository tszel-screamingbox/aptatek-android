package com.aptatek.pkulab.view.connect.scan.adapter;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.view.base.AdapterItem;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ScanDeviceAdapterItem implements AdapterItem {

    @NonNull
    public abstract ReaderDevice getReaderDevice();

    public String getName() {
        return getReaderDevice().getName();
    }

    public String getMacAddress() {
        return getReaderDevice().getMac();
    }

    @Override
    public Object uniqueIdentifier() {
        return getMacAddress();
    }

    public abstract Builder toBuilder();

    public static ScanDeviceAdapterItem.Builder builder() {
        return new AutoValue_ScanDeviceAdapterItem.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder setReaderDevice(@NonNull final ReaderDevice readerDevice);

        public abstract ScanDeviceAdapterItem build();
    }
}
