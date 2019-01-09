package com.aptatek.pkulab.device.bluetooth.characteristics.writer;

import android.support.annotation.NonNull;

public abstract class CharacteristicDataConverter {

    public abstract byte[] convertData(@NonNull Object data);


}
