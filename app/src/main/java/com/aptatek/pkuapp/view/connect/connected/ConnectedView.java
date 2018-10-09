package com.aptatek.pkuapp.view.connect.connected;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.model.ReaderDevice;
import com.aptatek.pkuapp.view.connect.common.BaseConnectScreenView;

public interface ConnectedView extends BaseConnectScreenView {

    void displayReaderDevice(@NonNull ReaderDevice device, int batteryLevel);

}
