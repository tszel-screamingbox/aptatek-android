package com.aptatek.pkuapp.injection.module.connect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;

import com.aptatek.pkuapp.device.bluetooth.BluetoothScannerImpl;
import com.aptatek.pkuapp.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkuapp.domain.manager.reader.BluetoothScanner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class ConnectModule {

    @Provides
    public BluetoothLeScanner provideBluetoothLeScanner() {
        return BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
    }

    @Provides
    public ScanSettings provideScanSettings() {
        return new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(0)
                .build();
    }

    @Provides
    public List<ScanFilter> provideScanFilters() {
        final List<ScanFilter> scanFilters = new ArrayList<>();
        final ScanFilter filter = new ScanFilter.Builder()
                .setDeviceName(LumosReaderConstants.DEVICE_NAME)
                .build();
        scanFilters.add(filter);

        // why don't you work :( ?
        // final ParcelUuid uuid = ParcelUuid.fromString(LumosReaderConstants.READER_SERVICE);
        // scanFilters.add(new ScanFilter.Builder().setServiceUuid(uuid).build());

        return Collections.unmodifiableList(scanFilters);
    }

    @Provides
    public BluetoothScanner provideBluetoothScanner(final BluetoothLeScanner leScanner,
                                                    final ScanSettings scanSettings,
                                                    final List<ScanFilter> scanFilters) {
        return new BluetoothScannerImpl(scanSettings, scanFilters, leScanner);
    }

}
