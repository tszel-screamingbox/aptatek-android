package com.aptatek.pkulab.view.connect.connected;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.model.ReaderDevice;
import com.aptatek.pkulab.view.connect.common.BaseConnectScreenView;

public interface ConnectedView extends BaseConnectScreenView {

    void displayReaderDevice(@NonNull ReaderDevice device, int batteryLevel);

}
