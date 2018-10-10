package com.aptatek.pkulab.injection.module;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.os.ParcelUuid;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.device.bluetooth.reader.LumosReaderManager;
import com.aptatek.pkulab.device.bluetooth.reader.ReaderManagerImpl;
import com.aptatek.pkulab.device.bluetooth.server.TimeServerImpl;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.domain.manager.reader.TimeServer;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.UUID;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = DeviceCommunicationModule.class)
public class DeviceModule {

    @Singleton
    @Provides
    public ReaderManager provideReaderManager(final LumosReaderManager lumosReaderManager) {
        return new ReaderManagerImpl(lumosReaderManager);
    }

    @Singleton
    @Provides
    public Gson provideGson() {
        return new GsonBuilder().create();
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

    @Singleton
    @Provides
    public AdvertiseData provideAdvertiseData() {
        return new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .addServiceUuid(ParcelUuid.fromString(LumosReaderConstants.TIME_SERVICE))
                .build();
    }

    @Singleton
    @Provides
    public BluetoothGattService provideTimeService() {
        final BluetoothGattService service = new BluetoothGattService(UUID.fromString(LumosReaderConstants.TIME_SERVICE), BluetoothGattService.SERVICE_TYPE_PRIMARY);
        final BluetoothGattCharacteristic currentTime = new BluetoothGattCharacteristic(UUID.fromString(LumosReaderConstants.TIME_SERVICE_CHAR_CURRENT_TIME),BluetoothGattCharacteristic.PROPERTY_READ, BluetoothGattCharacteristic.PERMISSION_READ);
        final BluetoothGattCharacteristic localTime = new BluetoothGattCharacteristic(UUID.fromString(LumosReaderConstants.TIME_SERVICE_CHAR_LOCAL_TIME_INFO),BluetoothGattCharacteristic.PROPERTY_READ,BluetoothGattCharacteristic.PERMISSION_READ);
        service.addCharacteristic(currentTime);
        service.addCharacteristic(localTime);

        return service;
    }

    @Singleton
    @Provides
    public TimeServer provideTimeServer(@ApplicationContext final Context context,
                                        final BluetoothManager bluetoothManager,
                                        final AdvertiseSettings advertiseSettings,
                                        final AdvertiseData advertiseData,
                                        final BluetoothGattService timeService) {
        return new TimeServerImpl(context, bluetoothManager, advertiseSettings, advertiseData, timeService);
    }
}
