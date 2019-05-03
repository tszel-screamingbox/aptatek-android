package com.aptatek.pkulab.device.bluetooth.characteristics.writer;

import androidx.annotation.Nullable;

public interface CharacteristicDataProvider<T extends CharacteristicDataProvider.CharacteristicsData> {

    byte[] provideData(@Nullable T data);

    interface CharacteristicsData { }

}
