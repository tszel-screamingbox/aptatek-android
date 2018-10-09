package com.aptatek.pkulab.injection.module.scan;

import com.aptatek.pkulab.device.bluetooth.BluetoothScannerImpl;
import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.domain.manager.reader.BluetoothScanner;

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

}
