package com.aptatek.pkulab.device.bluetooth.characteristics.writer;

import android.support.annotation.Nullable;

import javax.inject.Inject;

public class SyncRequestCharacteristicDataProvider implements CharacteristicDataProvider<CharacteristicDataProvider.CharacteristicsData> {

    @Inject
    public SyncRequestCharacteristicDataProvider() {
    }

    @Override
    public byte[] provideData(@Nullable CharacteristicsData data) {
        return new byte[] {0x0};
    }
}
