package com.aptatek.pkuapp.domain.manager.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.error.ReaderError;
import com.aptatek.pkuapp.domain.model.ReaderConnectionEvent;
import com.aptatek.pkuapp.domain.model.ReaderDevice;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;

public interface ReaderManager {

    void connect(@NonNull ReaderDevice readerDevice);

    void disconnect();

    void queryBatteryLevel();

    void queryCartridgeId();

    Flowable<ReaderConnectionEvent> connectionEvents();

    Flowable<ReaderError> readerErrors();

    Flowable<Integer> batteryLevel();

    Flowable<String> cartridgeId();

    // TODO gather other characteristics

}
