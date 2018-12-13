package com.aptatek.pkulab.injection.module;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.device.bluetooth.characteristics.reader.BatteryLevelReader;
import com.aptatek.pkulab.device.bluetooth.characteristics.reader.CartridgeIdReader;
import com.aptatek.pkulab.device.bluetooth.characteristics.reader.ErrorReader;
import com.aptatek.pkulab.device.bluetooth.characteristics.reader.NumResultsReader;
import com.aptatek.pkulab.device.bluetooth.characteristics.reader.ResultReader;
import com.aptatek.pkulab.device.bluetooth.characteristics.reader.SyncResponseReader;
import com.aptatek.pkulab.device.bluetooth.characteristics.reader.WorkflowStateReader;
import com.aptatek.pkulab.device.bluetooth.characteristics.reader.CharacteristicReader;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

@Module
public interface DeviceCommunicationModule {

    @Binds
    @IntoMap
    @StringKey(LumosReaderConstants.BATTERY_CHAR_LEVEL)
    CharacteristicReader bindBatteryLevelReader(final BatteryLevelReader reader);

    @Binds
    @IntoMap
    @StringKey(LumosReaderConstants.READER_CHAR_CARTRIDGE_ID)
    CharacteristicReader bindCartridgeReader(final CartridgeIdReader reader);

    @Binds
    @IntoMap
    @StringKey(LumosReaderConstants.READER_CHAR_NUM_RESULTS)
    CharacteristicReader bindNumResultsReader(final NumResultsReader reader);
    
    @Binds
    @IntoMap
    @StringKey(LumosReaderConstants.READER_CHAR_RESULT)
    CharacteristicReader bindResultReader(final ResultReader reader);
    
    @Binds
    @IntoMap
    @StringKey(LumosReaderConstants.READER_CHAR_WORKFLOW_STATE)
    CharacteristicReader bindWorkflowStateReader(final WorkflowStateReader reader);
    
    @Binds
    @IntoMap
    @StringKey(LumosReaderConstants.READER_CHAR_ERROR)
    CharacteristicReader bindErrorReader(final ErrorReader reader);

    @Binds
    @IntoMap
    @StringKey(LumosReaderConstants.READER_CHAR_RESULT_SYNC_RESPONSE)
    CharacteristicReader bindSyncResponseReader(final SyncResponseReader reader);

}
