package com.aptatek.pkulab.device.bluetooth.model.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultSyncAdapter extends TypeAdapter<List<String>> {

    @Override
    public void write(JsonWriter out, List<String> value) throws IOException {
        // currently we don't use this for serialization
    }

    @Override
    public List<String> read(JsonReader in) throws IOException {
        final List<String> list = new ArrayList<>();

        in.beginArray();
        while (in.hasNext()) {
            list.add(in.nextString());
        }
        in.endArray();

        return list;
    }
}
