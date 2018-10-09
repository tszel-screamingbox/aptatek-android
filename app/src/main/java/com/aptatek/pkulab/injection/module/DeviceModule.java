package com.aptatek.pkulab.injection.module;

import com.aptatek.pkulab.device.bluetooth.LumosReaderManager;
import com.aptatek.pkulab.device.bluetooth.ReaderManagerImpl;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
}
