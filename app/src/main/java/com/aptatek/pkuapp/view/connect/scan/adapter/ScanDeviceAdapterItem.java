package com.aptatek.pkuapp.view.connect.scan.adapter;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.model.ReaderDevice;
import com.aptatek.pkuapp.view.base.AdapterItem;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ScanDeviceAdapterItem implements AdapterItem {

    @NonNull
    public abstract ReaderDevice getReaderDevice();

    public abstract boolean isEnabled();

    public abstract boolean isConnectingToThis();

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
        return new AutoValue_ScanDeviceAdapterItem.Builder()
                .setEnabled(true)
                .setConnectingToThis(false);
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder setReaderDevice(@NonNull final ReaderDevice readerDevice);

        public abstract Builder setEnabled(final boolean enabled);

        public abstract Builder setConnectingToThis(final boolean connectingToThis);

        public abstract ScanDeviceAdapterItem build();
    }
}
