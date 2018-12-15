package com.aptatek.pkulab.device.bluetooth.characteristics.reader.deserializer;

import com.aptatek.pkulab.device.bluetooth.model.ResultSyncResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ResultSyncResponseDeserializer implements JsonDeserializer<ResultSyncResponse> {

    @Inject
    public ResultSyncResponseDeserializer() {
    }

    @Override
    public ResultSyncResponse deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final ResultSyncResponse resultSyncResponse = new ResultSyncResponse();

        final JsonArray asJsonArray = json.getAsJsonArray();
        final JsonElement firstElement = asJsonArray.get(0);
        if (firstElement.isJsonObject()) {
            final JsonObject nResultsJsonObject = firstElement.getAsJsonObject();
            final int nResults = nResultsJsonObject.get("nResults").getAsInt();
            resultSyncResponse.setNumberOfResults(nResults);
        }

        final List<String> ids = new ArrayList<>();
        final JsonElement secondElement = asJsonArray.get(1);
        if (secondElement.isJsonArray()) {
            final JsonArray idsArray = secondElement.getAsJsonArray();
            for (int i = 0; i < idsArray.size(); i++) {
                ids.add(idsArray.get(i).getAsString());
            }
        }
        resultSyncResponse.setIdentifiers(ids);

        return resultSyncResponse;
    }
}
