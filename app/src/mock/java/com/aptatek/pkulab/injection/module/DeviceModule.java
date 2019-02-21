package com.aptatek.pkulab.injection.module;

import android.content.Context;

import com.aptatek.pkulab.data.generator.FakeReaderDataGenerator;
import com.aptatek.pkulab.data.generator.FakeReaderDeviceGenerator;
import com.aptatek.pkulab.device.bluetooth.reader.MockReaderManager;
import com.aptatek.pkulab.device.bluetooth.scanner.BluetoothConditionCheckerImpl;
import com.aptatek.pkulab.device.bluetooth.scanner.MockBluetootScanner;
import com.aptatek.pkulab.device.bluetooth.scanner.MockBluetoothAdapter;
import com.aptatek.pkulab.domain.manager.reader.BluetoothAdapter;
import com.aptatek.pkulab.domain.manager.reader.BluetoothConditionChecker;
import com.aptatek.pkulab.domain.manager.reader.BluetoothScanner;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = MockModule.class)
public class DeviceModule {

    @Singleton
    @Provides
    public ReaderManager provideReaderManager(final FakeReaderDataGenerator dataGenerator) {
        return new MockReaderManager(dataGenerator);
    }

    @Singleton
    @Provides
    public BluetoothScanner provideBluetoothScanner(final FakeReaderDeviceGenerator generator) {
        return new MockBluetootScanner(generator);
    }

    @Singleton
    @Provides
    public BluetoothAdapter provideAdapter() {
        return new MockBluetoothAdapter();
    }

    @Singleton
    @Provides
    public BluetoothConditionChecker provideConditionChecker(final @ApplicationContext Context context,
                                                             final BluetoothAdapter bluetoothAdapter) {
        return new BluetoothConditionCheckerImpl(context, bluetoothAdapter);
    }

}
