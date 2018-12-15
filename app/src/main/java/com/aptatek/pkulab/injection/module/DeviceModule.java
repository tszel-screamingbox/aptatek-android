package com.aptatek.pkulab.injection.module;

import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;

import com.aptatek.pkulab.device.bluetooth.characteristics.reader.deserializer.ResultSyncResponseDeserializer;
import com.aptatek.pkulab.device.bluetooth.model.ResultSyncResponse;
import com.aptatek.pkulab.device.bluetooth.reader.LumosReaderManager;
import com.aptatek.pkulab.device.bluetooth.reader.ReaderManagerImpl;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = DeviceCharacteristicReaderModule.class)
public class DeviceModule {

    @Singleton
    @Provides
    public ReaderManager provideReaderManager(final LumosReaderManager lumosReaderManager) {
        return new ReaderManagerImpl(lumosReaderManager);
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
