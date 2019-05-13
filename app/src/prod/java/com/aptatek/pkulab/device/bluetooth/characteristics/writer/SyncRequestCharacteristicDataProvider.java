package com.aptatek.pkulab.device.bluetooth.characteristics.writer;

import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.aptatek.pkulab.device.bluetooth.model.SyncAfterRequest;

import javax.inject.Inject;

public class SyncRequestCharacteristicDataProvider implements CharacteristicDataProvider<SyncRequestCharacteristicDataProvider.SyncAfterRequestData> {

    public static class SyncAfterRequestData implements CharacteristicDataProvider.CharacteristicsData {

        private final String after;

        public SyncAfterRequestData(final @Nullable String after) {
            this.after = after;
        }
    }

    private final JsonCharacteristicDataConverter characteristicDataConverter;

    @Inject
    public SyncRequestCharacteristicDataProvider(final JsonCharacteristicDataConverter characteristicDataConverter) {
        this.characteristicDataConverter = characteristicDataConverter;
    }

    @Override
    public byte[] provideData(final @Nullable SyncAfterRequestData data) {
        if (data == null || TextUtils.isEmpty(data.after)) {
            return new byte[]{0x0};
        } else {
            return characteristicDataConverter.convertData(createSyncAfterRequest(data.after));
        }
    }

    private SyncAfterRequest createSyncAfterRequest(final String after) {
        SyncAfterRequest syncAfterRequest = new SyncAfterRequest();
        syncAfterRequest.setAfter(after);

        return syncAfterRequest;
    }
}
