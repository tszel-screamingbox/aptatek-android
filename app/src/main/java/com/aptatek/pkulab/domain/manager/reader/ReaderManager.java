package com.aptatek.pkulab.domain.manager.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.error.ReaderError;
import com.aptatek.pkulab.domain.model.ReaderConnectionEvent;
import com.aptatek.pkulab.domain.model.ReaderDevice;

import io.reactivex.Flowable;

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
