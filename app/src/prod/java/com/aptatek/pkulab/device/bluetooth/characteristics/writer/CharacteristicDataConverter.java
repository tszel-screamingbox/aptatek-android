package com.aptatek.pkulab.device.bluetooth.characteristics.writer;

import androidx.annotation.NonNull;

public abstract class CharacteristicDataConverter {

    public abstract byte[] convertData(@NonNull Object data);


}
