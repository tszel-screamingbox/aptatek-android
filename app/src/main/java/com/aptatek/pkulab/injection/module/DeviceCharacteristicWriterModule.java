package com.aptatek.pkulab.injection.module;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.device.bluetooth.characteristics.writer.CharacteristicDataProvider;
import com.aptatek.pkulab.device.bluetooth.characteristics.writer.RequestResultCharacteristicDataProvider;
import com.aptatek.pkulab.device.bluetooth.characteristics.writer.SyncRequestCharacteristicDataProvider;
import com.aptatek.pkulab.device.bluetooth.characteristics.writer.TimeCharacteristicDataProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

@Module
public interface DeviceCharacteristicWriterModule {

    @Binds
    @IntoMap
    @StringKey(LumosReaderConstants.READER_CHAR_UPDATE_TIME)
    CharacteristicDataProvider bindUpdateTimeProvider(TimeCharacteristicDataProvider dataProvider);

    @Binds
    @IntoMap
    @StringKey(LumosReaderConstants.READER_CHAR_REQUEST_RESULT)
    CharacteristicDataProvider bindRequestResultProvider(RequestResultCharacteristicDataProvider dataProvider);
    @Binds
    @IntoMap
    @StringKey(LumosReaderConstants.READER_CHAR_RESULT_SYNC_REQUEST)
    CharacteristicDataProvider bindSyncRequestProvider(SyncRequestCharacteristicDataProvider dataProvider);

}
