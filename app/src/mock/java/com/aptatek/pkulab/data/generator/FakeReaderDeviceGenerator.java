package com.aptatek.pkulab.data.generator;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.data.model.MockReaderDevice;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;

import org.fluttercode.datafactory.impl.DataFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class FakeReaderDeviceGenerator {

    private static final int MAX_DEVICES = 4;
    private static final String DEVICE_NAME = "Mock Lumos Camera %d";

    private final DataFactory dataFactory;

    @Inject
    FakeReaderDeviceGenerator(final DataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    @NonNull
    public List<ReaderDevice> generateReaderDevices() {
        final List<ReaderDevice> readerDevices = new ArrayList<>();
        final int devices = dataFactory.getNumberBetween(0, MAX_DEVICES);

        for (int i = 0; i < devices; i++) {
            readerDevices.add(generateReaderDevice(i));
        }

        return readerDevices;
    }

    @NonNull
    private ReaderDevice generateReaderDevice(final int index) {
        return new MockReaderDevice(String.format(Locale.getDefault(), DEVICE_NAME, index), dataFactory.getRandomChars(10));
    }
}
