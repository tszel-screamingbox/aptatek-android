package com.aptatek.pkulab.injection.module;

import com.aptatek.pkulab.device.bluetooth.reader.CharacteristicReader;
import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.device.bluetooth.parser.CartridgeIdReader;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

@Module
public interface DeviceCommunicationModule {

    @Binds
    @IntoMap
    @StringKey(LumosReaderConstants.READER_CHAR_CARTRIDGE_ID)
    CharacteristicReader bindCartridgeReader(final CartridgeIdReader cartridgeIdReader);

}
