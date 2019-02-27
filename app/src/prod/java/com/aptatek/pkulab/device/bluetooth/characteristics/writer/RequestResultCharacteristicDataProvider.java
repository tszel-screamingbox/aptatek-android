package com.aptatek.pkulab.device.bluetooth.characteristics.writer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aptatek.pkulab.device.bluetooth.model.RequestResultRequest;

import javax.inject.Inject;

public class RequestResultCharacteristicDataProvider implements CharacteristicDataProvider<RequestResultCharacteristicDataProvider.RequestResultData> {

    public static class RequestResultData implements CharacteristicDataProvider.CharacteristicsData {

        private final String id;

        public RequestResultData(final @NonNull String id) {
            this.id = id;
        }

    }

    @Inject
    public RequestResultCharacteristicDataProvider(final JsonCharacteristicDataConverter characteristicDataConverter) {
        this.characteristicDataConverter = characteristicDataConverter;
    }

    private final JsonCharacteristicDataConverter characteristicDataConverter;

    @Override
    public byte[] provideData(@Nullable final RequestResultData data) {
        return characteristicDataConverter.convertData(createRequestResultRequest(data.id));
    }

    private RequestResultRequest createRequestResultRequest(@NonNull final String id) {
        final RequestResultRequest requestResultRequest = new RequestResultRequest();
        requestResultRequest.setResultId(id);

        return requestResultRequest;
    }
}
