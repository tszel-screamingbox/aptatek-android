package com.aptatek.pkulab.injection.module;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.os.ParcelUuid;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.device.bluetooth.characteristics.reader.deserializer.ResultSyncResponseDeserializer;
import com.aptatek.pkulab.device.bluetooth.model.ResultSyncResponse;
import com.aptatek.pkulab.device.bluetooth.reader.LumosReaderManager;
import com.aptatek.pkulab.device.bluetooth.reader.ReaderManagerImpl;
import com.aptatek.pkulab.device.bluetooth.scanner.BluetoothAdapterImpl;
import com.aptatek.pkulab.device.bluetooth.scanner.BluetoothConditionCheckerImpl;
import com.aptatek.pkulab.device.bluetooth.scanner.BluetoothScannerImpl;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.manager.reader.BluetoothConditionChecker;
import com.aptatek.pkulab.domain.manager.reader.BluetoothScanner;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

@Module(includes = {DeviceCharacteristicReaderModule.class, DeviceCharacteristicWriterModule.class, DeviceMapperModule.class})
public class DeviceModule {

    @Singleton
    @Provides
    public BluetoothLeScannerCompat provideBluetoothLeScanner() {
        return BluetoothLeScannerCompat.getScanner();
    }

    @Singleton
    @Provides
    public ScanSettings provideScanSettings() {
        return new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(0)
                .setUseHardwareFilteringIfSupported(true)
                .build();
    }

    @Singleton
    @Provides
    public List<ScanFilter> provideScanFilters() {
        final List<ScanFilter> scanFilters = new ArrayList<>();
        final ScanFilter filter = new ScanFilter.Builder()
                .setServiceUuid(ParcelUuid.fromString(LumosReaderConstants.READER_SERVICE))
                .build();
        scanFilters.add(filter);

        return Collections.unmodifiableList(scanFilters);
    }

    @Singleton
    @Provides
    public BluetoothScanner provideBluetoothScanner(final BluetoothLeScannerCompat leScanner,
                                                    final ScanSettings scanSettings,
                                                    final List<ScanFilter> scanFilters) {
        return new BluetoothScannerImpl(scanSettings, scanFilters, leScanner);
    }

    @Singleton
    @Provides
    public com.aptatek.pkulab.domain.manager.reader.BluetoothAdapter provideAdapter() {
        return new BluetoothAdapterImpl(BluetoothAdapter.getDefaultAdapter());
    }

    @Singleton
    @Provides
    public BluetoothConditionChecker provideConditionChecker(final @ApplicationContext Context context,
                                                             final com.aptatek.pkulab.domain.manager.reader.BluetoothAdapter bluetoothAdapter) {
        return new BluetoothConditionCheckerImpl(context, bluetoothAdapter);
    }

    @Singleton
    @Provides
    public ReaderManager provideReaderManager(final LumosReaderManager lumosReaderManager,
                                              final Map<Class<?>, Mapper<?, ?>> mappers) {
        return new ReaderManagerImpl(lumosReaderManager, mappers);
    }

    @Singleton
    @Provides
    public Gson provideGson(final ResultSyncResponseDeserializer deserializer) {
        return new GsonBuilder()
                .registerTypeAdapter(ResultSyncResponse.class, deserializer)
                .create();
    }

    @Singleton
    @Provides
    public BluetoothManager provideBluetoothManager(@ApplicationContext Context context) {
        return (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    @Singleton
    @Provides
    public AdvertiseSettings provideAdvertiseSettings() {
        return new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setConnectable(true)
                .setTimeout(0)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .build();
    }
}
