package com.aptatek.pkulab.injection.module.scan;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.aptatek.pkulab.device.bluetooth.scanner.BluetoothAdapterImpl;
import com.aptatek.pkulab.device.bluetooth.scanner.BluetoothConditionCheckerImpl;
import com.aptatek.pkulab.device.bluetooth.scanner.BluetoothScannerImpl;
import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.domain.manager.reader.BluetoothConditionChecker;
import com.aptatek.pkulab.domain.manager.reader.BluetoothScanner;
import com.aptatek.pkulab.injection.qualifier.ActivityContext;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

@Module
public class ScanModule {

    @Provides
    public BluetoothLeScannerCompat provideBluetoothLeScanner() {
        return BluetoothLeScannerCompat.getScanner();
    }

    @Provides
    public ScanSettings provideScanSettings() {
        return new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(0)
                .setUseHardwareFilteringIfSupported(true)
                .build();
    }

    @Provides
    public List<ScanFilter> provideScanFilters() {
        final List<ScanFilter> scanFilters = new ArrayList<>();
        final ScanFilter nameFilter = new ScanFilter.Builder()
                .setDeviceName(LumosReaderConstants.DEVICE_NAME)
                .build();
        scanFilters.add(nameFilter);

        // why don't you work :( ?
        // final ParcelUuid uuid = ParcelUuid.fromString(LumosReaderConstants.READER_SERVICE);
        // scanFilters.add(new ScanFilter.Builder().setServiceUuid(uuid).build());

        return Collections.unmodifiableList(scanFilters);
    }

    @Provides
    public BluetoothScanner provideBluetoothScanner(final BluetoothLeScannerCompat leScanner,
                                                    final ScanSettings scanSettings,
                                                    final List<ScanFilter> scanFilters) {
        return new BluetoothScannerImpl(scanSettings, scanFilters, leScanner);
    }

    @Provides
    public com.aptatek.pkulab.domain.manager.reader.BluetoothAdapter provideAdapter() {
        return new BluetoothAdapterImpl(BluetoothAdapter.getDefaultAdapter());
    }

    @Provides
    public BluetoothConditionChecker provideConditionChecker(final @ApplicationContext Context context,
                                                             final com.aptatek.pkulab.domain.manager.reader.BluetoothAdapter bluetoothAdapter) {
        return new BluetoothConditionCheckerImpl(context, bluetoothAdapter);
    }
}
