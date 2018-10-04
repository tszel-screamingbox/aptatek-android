package com.aptatek.pkuapp.view.connect.scan;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.model.ReaderDevice;
import com.aptatek.pkuapp.view.connect.common.BaseConnectScreenView;
import com.aptatek.pkuapp.view.connect.scan.adapter.ScanDeviceAdapterItem;

import java.util.List;

public interface ScanView extends BaseConnectScreenView {

    void displayScanResults(@NonNull List<ScanDeviceAdapterItem> devices);

    void showLoading(boolean loading);

    void showConnected(@NonNull ReaderDevice readerDevice);

    void showErrorToast(@NonNull String message);

}
