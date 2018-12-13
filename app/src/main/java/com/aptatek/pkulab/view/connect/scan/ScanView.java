package com.aptatek.pkulab.view.connect.scan;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.view.connect.common.BaseConnectScreenView;
import com.aptatek.pkulab.view.connect.scan.adapter.ScanDeviceAdapterItem;

import java.util.List;

public interface ScanView extends BaseConnectScreenView {

    void displayScanResults(@NonNull List<ScanDeviceAdapterItem> devices);

    void showLoading(boolean loading);

    void showConnected(@NonNull ReaderDevice readerDevice);

    void showErrorToast(@NonNull String message);

    void showMtuSizeChanged(int mtuSize);

    void showMtuError();

}
