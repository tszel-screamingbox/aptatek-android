package com.aptatek.pkuapp.view.connect.scan;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.model.ReaderDevice;
import com.aptatek.pkuapp.view.connect.common.BaseConnectScreenView;

import java.util.Set;

public interface ScanView extends BaseConnectScreenView {

    void displayScanResults(@NonNull Set<ReaderDevice> devices);

    void showLoading(boolean loading);

}
